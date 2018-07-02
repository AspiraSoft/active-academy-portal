package org.activeacademy.portal.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.activeacademy.portal.R;
import org.activeacademy.portal.models.TimetableEntry;
import org.activeacademy.portal.view.TimetableEntryView;

import java.util.List;

public class TimetableAdapter extends ArrayAdapter<TimetableEntry> {

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
            convertView = vi.inflate(R.layout.list_item_timetable, parent, false);

            mView.setCourseView((TextView) convertView.findViewById(R.id.courseName));
            mView.setGradeView((TextView) convertView.findViewById(R.id.gradeName));
            mView.setTimeView((TextView) convertView.findViewById(R.id.lectureTime));

            convertView.setTag(mView);
        } else {
            mView = (TimetableEntryView) convertView.getTag();
        }

        mView.show(entries.get(position));
        return convertView;
    }
}
