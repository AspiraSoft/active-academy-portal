package org.activeacademy.portal.view;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.activeacademy.portal.R;
import org.activeacademy.portal.models.TimetableEntry;

public class TimetableEntryView {

    private TextView mCourseView;
    private TextView mGradeView;
    private TextView mTimeView;
    private LinearLayout mDayView;

    public void setCourseView(TextView mCourseView) {
        this.mCourseView = mCourseView;
    }

    public void setGradeView(TextView mGradeView) {
        this.mGradeView = mGradeView;
    }

    public void setTimeView(TextView mTimeView) {
        this.mTimeView = mTimeView;
    }

    public void setDayView(LinearLayout mDayView) {
        this.mDayView = mDayView;
    }

    public void show(TimetableEntry entry) {
        if (mCourseView != null)
            mCourseView.setText(entry.getCourseName());

        if (mGradeView != null)
            mGradeView.setText(entry.getGradeName());

        if (mTimeView != null)
            mTimeView.setText(entry.getLectureTime());

        if (mDayView != null)
            try {
                deselectAll();
                switch (entry.getLectureDay()) {
                    case "Mon":
                    case "Monday":
                        select(mDayView.getChildAt(0));
                        break;
                    case "Tue":
                    case "Tuesday":
                        select(mDayView.getChildAt(1));
                        break;
                    case "Wed":
                    case "Wednesday":
                        select(mDayView.getChildAt(2));
                        break;
                    case "Thur":
                    case "Thursday":
                        select(mDayView.getChildAt(3));
                        break;
                    case "Fri":
                    case "Friday":
                        select(mDayView.getChildAt(4));
                        break;
                    case "Sat":
                    case "Saturday":
                        select(mDayView.getChildAt(5));
                        break;
                    case "Sun":
                    case "Sunday":
                        select(mDayView.getChildAt(6));
                        break;
                }
            } catch (Exception ex) {
                mDayView.setVisibility(View.GONE);
            }
    }

    private void deselectAll() {
        if (mDayView != null) {
            for (int i = 0; i < mDayView.getChildCount(); i++) {
                deselect(mDayView.getChildAt(i));
            }
        }
    }

    private void deselect(View dayView) {
        dayView.setBackgroundResource(R.drawable.panel_primary_no_outline);
    }

    private void select(View dayView) {
        dayView.setBackgroundResource(R.drawable.panel_secondary);
    }

}
