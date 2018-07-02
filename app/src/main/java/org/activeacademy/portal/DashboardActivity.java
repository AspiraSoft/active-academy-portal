package org.activeacademy.portal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.activeacademy.portal.auth.LoginManager;
import org.activeacademy.portal.db.RemoteDatabase;
import org.activeacademy.portal.models.Course;
import org.activeacademy.portal.models.Grade;
import org.activeacademy.portal.models.Instructor;
import org.activeacademy.portal.models.Lecture;
import org.activeacademy.portal.utils.OnRemoteObjectReceivedListener;
import org.activeacademy.portal.utils.OnRemoteObjectsReceivedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        carouselView.setSlideInterval(10000);
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
    protected void onStart() {
        super.onStart();
        if (!mLoginManager.isUserSignedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Fetch personal information
        findViewById(R.id.dashboard_row1).setVisibility(View.INVISIBLE);
        findViewById(R.id.dashboard_row2).setVisibility(View.INVISIBLE);
        findViewById(R.id.dashboard_row3).setVisibility(View.INVISIBLE);
        findViewById(R.id.dashboard_loading).setVisibility(View.VISIBLE);
        mRemoteDb.getInstructorDetails(this);

        // Fetch class schedule
        findViewById(R.id.schedule).setVisibility(View.GONE);
        findViewById(R.id.schedule_loading).setVisibility(View.VISIBLE);
        mRemoteDb.getTimetable(new OnRemoteObjectsReceivedListener<Grade>() {
            @Override
            public void onReceiveSuccess(@NonNull List<Grade> gradeList) {
                findViewById(R.id.schedule).setVisibility(View.VISIBLE);
                findViewById(R.id.schedule_loading).setVisibility(View.GONE);

                // What is the day today?
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());

                // Get a list of lectures scheduled for today
                final ArrayList<TimetableEntry> timetableEntries = new ArrayList<>();
                for (Grade grade : gradeList) {
                    if (grade.getCourses() != null) {
                        for (Course course : grade.getCourses()) {
                            if (course.getLectures() != null) {
                                for (Lecture lecture : course.getLectures()) {
                                    if (lecture.getDay().equalsIgnoreCase(today)) {
                                        TimetableEntry entry = new TimetableEntry();
                                        entry.courseName = course.getName();
                                        entry.gradeName = grade.getName();
                                        entry.lectureTime = lecture.getStartTime() + " - " + lecture.getEndTime();

                                        timetableEntries.add(entry);
                                    }
                                }
                            }
                        }
                    }
                }

                TimetableAdapter adapter = new TimetableAdapter(
                        DashboardActivity.this,
                        timetableEntries);

                ListView scheduleView = findViewById(R.id.schedule);
                scheduleView.setAdapter(adapter);
            }

            @Override
            public void onReceiveError() {
                findViewById(R.id.schedule).setVisibility(View.VISIBLE);
                findViewById(R.id.schedule_loading).setVisibility(View.GONE);

                Log.d(AcademyApplication.TAG, "Error retrieving schedule");
            }
        });
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
        findViewById(R.id.dashboard_row3).setVisibility(View.VISIBLE);
        findViewById(R.id.dashboard_error).setVisibility(View.GONE);
        findViewById(R.id.dashboard_loading).setVisibility(View.GONE);
    }

    @Override
    public void onReceiveError() {
        findViewById(R.id.dashboard_error).setVisibility(View.VISIBLE);
        findViewById(R.id.dashboard_loading).setVisibility(View.GONE);
    }

    private static class TimetableEntryView {

        private TextView mCourseView;
        private TextView mGradeView;
        private TextView mTimeView;

    }

    private class TimetableAdapter extends ArrayAdapter<TimetableEntry> {

        private final Context context;
        private final List<TimetableEntry> entries;

        public TimetableAdapter(@NonNull Context context, @NonNull List<TimetableEntry> entries) {
            super(context, -1, entries);
            this.context = context;
            this.entries = entries;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            TimetableEntryView mView = null;
            if (convertView == null) {
                mView = new TimetableEntryView();

                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.list_item_lecture_single, parent, false);

                mView.mCourseView = convertView.findViewById(R.id.courseName);
                mView.mGradeView = convertView.findViewById(R.id.gradeName);
                mView.mTimeView = convertView.findViewById(R.id.lectureTime);

                convertView.setTag(mView);
            } else {
                mView = (TimetableEntryView) convertView.getTag();
            }

            TimetableEntry entry = entries.get(position);

            mView.mCourseView.setText(entry.courseName);
            mView.mGradeView.setText(entry.gradeName);
            mView.mTimeView.setText(entry.lectureTime);

            return convertView;
        }
    }

    private class TimetableEntry {
        private String lectureTime;
        private String gradeName;
        private String courseName;
    }

}