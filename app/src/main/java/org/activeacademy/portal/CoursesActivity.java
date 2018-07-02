package org.activeacademy.portal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

import org.activeacademy.portal.auth.LoginManager;
import org.activeacademy.portal.utils.CoursesViewController;
import org.activeacademy.portal.utils.RefreshListener;
import org.activeacademy.portal.utils.TimetableManager;

public class CoursesActivity extends AppCompatActivity implements RefreshListener {

    private CoursesViewController mCoursesViewController;

    private TimetableManager mTimetableManager;
    private LoginManager mLoginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        mCoursesViewController = new CoursesViewController(this);
        mCoursesViewController.setOnRefreshComplete(this);

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

    }

    private void refresh() {
        mTimetableManager.getLecturesAll(mCoursesViewController);
    }

}