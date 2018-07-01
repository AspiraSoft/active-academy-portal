package org.activeacademy.portal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.activeacademy.portal.auth.LoginManager;
import org.activeacademy.portal.db.RemoteDatabase;
import org.activeacademy.portal.models.Instructor;
import org.activeacademy.portal.utils.OnRemoteObjectReceivedListener;

public class DashboardActivity extends AppCompatActivity
        implements OnRemoteObjectReceivedListener<Instructor> {

    private int[] bannerImages = {
            R.drawable.carousel_0,
            R.drawable.carousel_1,
            R.drawable.carousel_2
    };

    private LoginManager mLoginManager;
    private RemoteDatabase mRemoteDb;

    private TextView mNameView;
    private TextView mPhoneView;
    private TextView mEmailView;
    private TextView mPasswordView;
    private TextView mPositionView;
    private TextView mJoinDateView;
    private TextView mSalaryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set up actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Get UI references
        mNameView = findViewById(R.id.name);
        mPhoneView = findViewById(R.id.phone);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mPositionView = findViewById(R.id.position);
        mJoinDateView = findViewById(R.id.join_date);
        mSalaryView = findViewById(R.id.salary);

        // Configure banner carousel
        CarouselView carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(bannerImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(bannerImages[position]);
            }
        });

        mLoginManager = LoginManager.getInstance();
        mRemoteDb = RemoteDatabase.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mLoginManager.isUserSignedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        findViewById(R.id.dashboard_row1).setVisibility(View.INVISIBLE);
        findViewById(R.id.dashboard_row2).setVisibility(View.INVISIBLE);
        findViewById(R.id.dashboard_loading).setVisibility(View.VISIBLE);
        mRemoteDb.getRemoteObject(Instructor.class, this);
    }

    @Override
    protected void onDestroy() {
        LoginManager.getInstance().signOut();
        super.onDestroy();
    }

    @Override
    public void onReceiveSuccess(@NonNull Instructor instructor) {
        mNameView.setText(instructor.getName());
        mPhoneView.setText(instructor.getPhone());
        mEmailView.setText(instructor.getEmail());
        mPositionView.setText(instructor.getPosition());
        mJoinDateView.setText("since " + instructor.getJoinDate());
        mSalaryView.setText("at PKR " + String.valueOf(instructor.getSalary()) + "/-");

        findViewById(R.id.dashboard_row1).setVisibility(View.VISIBLE);
        findViewById(R.id.dashboard_row2).setVisibility(View.VISIBLE);
        findViewById(R.id.dashboard_loading).setVisibility(View.GONE);
    }

    @Override
    public void onReceiveError() {

    }

}