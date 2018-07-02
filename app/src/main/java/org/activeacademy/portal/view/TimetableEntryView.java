package org.activeacademy.portal.view;

import android.widget.TextView;

import org.activeacademy.portal.models.TimetableEntry;

public class TimetableEntryView {

    private TextView mCourseView;
    private TextView mGradeView;
    private TextView mTimeView;

    public void setCourseView(TextView mCourseView) {
        this.mCourseView = mCourseView;
    }

    public void setGradeView(TextView mGradeView) {
        this.mGradeView = mGradeView;
    }

    public void setTimeView(TextView mTimeView) {
        this.mTimeView = mTimeView;
    }

    public void show(TimetableEntry entry) {
        mCourseView.setText(entry.getCourseName());
        mGradeView.setText(entry.getGradeName());
        mTimeView.setText(entry.getLectureTime());
    }

}
