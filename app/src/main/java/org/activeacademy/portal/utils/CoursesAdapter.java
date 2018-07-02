package org.activeacademy.portal.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.activeacademy.portal.R;
import org.activeacademy.portal.models.CourseEntry;
import org.activeacademy.portal.models.TimetableEntry;
import org.activeacademy.portal.view.CourseEntryView;
import org.activeacademy.portal.view.TimetableEntryView;

import java.util.ArrayList;
import java.util.List;

public class CoursesAdapter extends ArrayAdapter<CourseEntry> {

    private final Context context;
    private final List<CourseEntry> entries;

    public CoursesAdapter(@NonNull Context context, @NonNull List<CourseEntry> entries) {
        super(context, -1, entries);
        this.context = context;
        this.entries = entries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        CourseEntryView mView = null;
        CourseEntry mEntry = entries.get(position);
        if (convertView == null) {
            mView = new CourseEntryView();

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item_course, parent, false);

            mView.setCourseView((TextView) convertView.findViewById(R.id.courseName));
            mView.setGradeView((TextView) convertView.findViewById(R.id.gradeName));

            LinearLayout mCourseView = convertView.findViewById(R.id.courseView);
            List<TimetableEntryView> entryViews = new ArrayList<>();
            for (TimetableEntry mTimetableEntry : mEntry.getLectures()) {
                View mLectureView = vi.inflate(R.layout.list_item_lecture, mCourseView, false);
                mCourseView.addView(mLectureView);

                TimetableEntryView mTimetableEntryView = new TimetableEntryView();
                mTimetableEntryView.setCourseView((TextView) mLectureView.findViewById(R.id.courseName));
                mTimetableEntryView.setGradeView((TextView) mLectureView.findViewById(R.id.gradeName));
                mTimetableEntryView.setTimeView((TextView) mLectureView.findViewById(R.id.lectureTime));
                mTimetableEntryView.setDayView((LinearLayout) mLectureView.findViewById(R.id.lectureDay));

                entryViews.add(mTimetableEntryView);
            }

            mView.setLectureViews(entryViews);
            convertView.setTag(mView);
        } else {
            mView = (CourseEntryView) convertView.getTag();
        }

        mView.show(mEntry);
        return convertView;
    }

}