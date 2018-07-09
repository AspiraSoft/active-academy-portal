package org.activeacademy.portal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseUser;

import org.activeacademy.portal.auth.LoginManager;
import org.activeacademy.portal.errors.UnknownException;
import org.activeacademy.portal.utils.RefreshListener;
import org.activeacademy.portal.utils.TimetableManager;
import org.activeacademy.portal.utils.TimetableViewController;
import org.activeacademy.portal.utils.UserInfoViewController;

public class DashboardActivity extends AppCompatActivity implements
        RefreshListener, OnCompleteListener<Void> {

    private static final long FAILURE_TIMEOUT = 10;    // Refresh after 10s if last refresh failed
    private static final long SUCCESS_TIMEOUT = 300;   // Refresh after 5m if last refresh succeeded

    private TimetableViewController mTimetableViewController;
    private UserInfoViewController mUserInfoViewController;

    private TimetableManager mTimetableManager;
    private LoginManager mLoginManager;

    private MenuItem mRefreshIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set up actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mUserInfoViewController = new UserInfoViewController(this);
        mUserInfoViewController.setOnRefreshComplete(this);

        mTimetableViewController = new TimetableViewController(this);
        mTimetableViewController.setOnRefreshComplete(this);

        mLoginManager = LoginManager.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mLoginManager.getCurrentUser();
        if (currentUser == null || !mLoginManager.isUserSignedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        mTimetableManager = new TimetableManager(currentUser);
        refresh();
    }

    @Override
    public void onRefresh(RefreshStatus status) {
        long timeout = FAILURE_TIMEOUT * 1000;
        boolean failed = true;
        if (status == RefreshStatus.REFRESH_SUCCESS) {
            timeout = SUCCESS_TIMEOUT * 1000;
            failed = false;
        }

        final boolean finalFailed = failed;
        new CountDownTimer(timeout, 1000) {
            @Override
            public void onTick(long millsUntilFinished) {
                if (finalFailed) {
                    mUserInfoViewController.showError("Refreshing in " + String.valueOf(millsUntilFinished / 1000) + "s");
                }
            }

            @Override
            public void onFinish() {
                refresh();
            }
        }.start();
        mRefreshIcon.setVisible(true);
    }

    @Override
    protected void onDestroy() {
        LoginManager.getInstance().signOut();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        mRefreshIcon = menu.findItem(R.id.refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh: {
                mRefreshIcon.setVisible(false);
                refresh();
            }
        }
        return false;
    }

    private void refresh() {
        mLoginManager.getCurrentUserInfo(mUserInfoViewController);
        mTimetableManager.getLecturesToday(mTimetableViewController);
    }

    public void seeAllCourses(View view) {
        startActivity(new Intent(this, CoursesActivity.class));
    }

    public void resetPassword(final View view) {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("A password reset email will be sent to you and you will be logged out of your account.")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Disable reset button
                        view.setEnabled(false);

                        // Start asynchronous reset operation
                        Toast.makeText(DashboardActivity.this, "Initiating request ...", Toast.LENGTH_SHORT).show();
                        mLoginManager.resetPassword(DashboardActivity.this);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            Toast.makeText(this, "An email has been sent", Toast.LENGTH_LONG).show();

            mLoginManager.signOut();

            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            findViewById(R.id.password_change).setEnabled(true);
            Exception exception = task.getException();
            if (exception == null) {
                exception = new UnknownException();
            }

            try {
                throw exception;
            } catch (FirebaseNetworkException e) {
                Toast.makeText(this, "Check your network connection", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}