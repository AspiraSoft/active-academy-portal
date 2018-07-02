package org.activeacademy.portal.db;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.activeacademy.portal.AcademyApplication;
import org.activeacademy.portal.auth.LoginManager;
import org.activeacademy.portal.models.Course;
import org.activeacademy.portal.models.Grade;
import org.activeacademy.portal.models.Instructor;
import org.activeacademy.portal.utils.OnRemoteObjectReceivedListener;
import org.activeacademy.portal.utils.OnRemoteObjectsReceivedListener;

import java.util.ArrayList;
import java.util.List;

public class RemoteDatabase {

    private static final RemoteDatabase ourInstance = new RemoteDatabase();

    private final FirebaseDatabase mDatabase;
    private final LoginManager mLoginManager;

    private RemoteDatabase() {
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mLoginManager = LoginManager.getInstance();
    }

    public static RemoteDatabase getInstance() {
        return ourInstance;
    }

    public void getInstructorDetails(@NonNull final OnRemoteObjectReceivedListener listener) {
        FirebaseUser currentUser = mLoginManager.getCurrentUser();
        if (mLoginManager.isUserSignedIn() && currentUser != null) {
            DatabaseReference reference = mDatabase.getReference("users/" + currentUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        Instructor instructor = dataSnapshot.getValue(Instructor.class);
                        if (instructor != null) {
                            listener.onReceiveSuccess(instructor);
                        } else {
                            listener.onReceiveError();
                        }
                    } catch (Exception ex) {
                        listener.onReceiveError();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onReceiveError();
                }
            });
        } else {
            listener.onReceiveError();
        }
    }

    public void getTimetable(@NonNull final OnRemoteObjectsReceivedListener listener) {
        final FirebaseUser currentUser = mLoginManager.getCurrentUser();
        if (mLoginManager.isUserSignedIn() && currentUser != null) {
            DatabaseReference reference = mDatabase.getReference("classes/");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        GenericTypeIndicator<List<Grade>> t = new GenericTypeIndicator<List<Grade>>() {
                        };
                        List<Grade> gradeList = dataSnapshot.getValue(t);
                        for (Grade grade : gradeList) {
                            ArrayList<Course> courses = new ArrayList<>();
                            for (Course course : grade.getCourses()) {
                                if (course != null && course.getInstructorId().equals(currentUser.getUid())) {
                                    course.setName(course.getName() + " (" + grade.getName() + ")");
                                    courses.add(course);
                                }
                            }
                            grade.setCourses(courses);
                        }
                        listener.onReceiveSuccess(gradeList);
                    } catch (Exception ex) {
                        listener.onReceiveError();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(AcademyApplication.TAG, "Failed to get schedule");
                }
            });
        } else {
            Log.d(AcademyApplication.TAG, "Failed to get schedule");
        }
    }

}