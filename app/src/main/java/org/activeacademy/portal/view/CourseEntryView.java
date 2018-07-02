package org.activeacademy.portal.view;

import android.widget.TextView;

import org.activeacademy.portal.models.CourseEntry;

import java.util.List;

public class CourseEntryView {

    private TextView mCourseView;
    private TextView mGradeView;
    private List<TimetableEntryView> mLectureViews;

    public void setCourseView(TextView mCourseView) {
        this.mCourseView = mCourseView;
    }

    public void setGradeView(TextView mGradeView) {
        this.mGradeView = mGradeView;
    }

    public void setLectureViews(List<TimetableEntryView> mLectureViews) {
        this.mLectureViews = mLectureViews;
    }

    public void show(CourseEntry entry) {
        mCourseView.setText(entry.getCourseName());
        mGradeView.setText(entry.getGradeName());

        for (int i = 0; i < mLectureViews.size(); i++) {
            if (i > entry.getLectures().size()) {
                break;
            }

            TimetableEntryView lectureView = mLectureViews.get(i);
            lectureView.show(entry.getLectures().get(i));
        }
    }

}
