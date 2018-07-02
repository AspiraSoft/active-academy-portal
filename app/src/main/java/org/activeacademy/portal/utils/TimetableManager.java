package org.activeacademy.portal.utils;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.GenericTypeIndicator;

import org.activeacademy.portal.db.RemoteDatabase;
import org.activeacademy.portal.models.Course;
import org.activeacademy.portal.models.Grade;
import org.activeacademy.portal.models.Lecture;
import org.activeacademy.portal.models.TimetableEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author saifkhichi96
 * @version 1.0.0
 * @since 1.0.0 02/07/2018 3:09 PM
 */
public class TimetableManager {

    private final GenericTypeIndicator<List<Grade>> responseType;
    private final String currentUserId;

    public TimetableManager(@NonNull FirebaseUser currentUser) {
        this.currentUserId = currentUser.getUid();
        this.responseType = new GenericTypeIndicator<List<Grade>>() {
        };
    }

    private void getAsync(final ResponseHandler<List<Grade>> responseHandler) {
        RemoteDatabase.getInstance().getAsync("classes/", responseType,
                new ResponseHandler<List<Grade>>() {
                    @Override
                    public void onReceiveSuccess(@NonNull List<Grade> grades) {
                        for (Grade grade : grades) {
                            // Discard any courses not taught by current user
                            ArrayList<Course> validCourses = new ArrayList<>();
                            for (Course course : grade.getCourses()) {
                                if (course != null && course.getInstructorId().equals(currentUserId)) {
                                    validCourses.add(course);
                                }
                            }

                            grade.setCourses(validCourses);
                        }

                        responseHandler.onReceiveSuccess(grades);
                    }

                    @Override
                    public void onReceiveError(@NonNull Exception ex) {
                        responseHandler.onReceiveError(ex);
                    }
                });
    }

    public void getLecturesToday(final ResponseHandler<List<TimetableEntry>> responseHandler) {
        getAsync(new ResponseHandler<List<Grade>>() {
            @Override
            public void onReceiveSuccess(@NonNull List<Grade> grades) {
                // What is the day today?
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());

                // Get a list of lectures scheduled for today
                final ArrayList<TimetableEntry> timetableEntries = new ArrayList<>();
                for (Grade grade : grades) {
                    if (grade.getCourses() != null) {
                        for (Course course : grade.getCourses()) {
                            if (course.getLectures() != null) {
                                for (Lecture lecture : course.getLectures()) {
                                    if (lecture.getDay().equalsIgnoreCase(today)) {
                                        TimetableEntry entry = new TimetableEntry(grade, course, lecture);

                                        timetableEntries.add(entry);
                                    }
                                }
                            }
                        }
                    }
                }

                responseHandler.onReceiveSuccess(timetableEntries);
            }

            @Override
            public void onReceiveError(@NonNull Exception ex) {
                responseHandler.onReceiveError(ex);
            }
        });
    }

    public void getLecturesAll(final ResponseHandler<List<Grade>> responseHandler) {

    }

}
