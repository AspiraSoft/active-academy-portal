package org.activeacademy.portal.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.activeacademy.portal.DashboardActivity;
import org.activeacademy.portal.R;
import org.activeacademy.portal.models.TimetableEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TimetableViewController implements ResponseHandler<List<TimetableEntry>> {

    private final Context context;

    private final TextView mDefaultMessageView;
    private final ListView mScheduleView;
    private final TextView mDateView;

    private RefreshListener onRefreshComplete;

    public TimetableViewController(DashboardActivity activity) {
        this.context = activity;

        mDefaultMessageView = activity.findViewById(R.id.schedule_loading);
        mScheduleView = activity.findViewById(R.id.schedule);
        mDateView = activity.findViewById(R.id.today);

        setCurrentDate();
        showDefaultMessage();
    }

    public void setOnRefreshComplete(RefreshListener onRefreshComplete) {
        this.onRefreshComplete = onRefreshComplete;
    }

    private void setCurrentDate() {
        long now = Calendar.getInstance().getTime().getTime();
        String today = new SimpleDateFormat("E, d MMMM y", Locale.ENGLISH).format(now);
        mDateView.setText(today);
    }

    private void showDefaultMessage() {
        mScheduleView.setVisibility(View.INVISIBLE);
        mDefaultMessageView.setVisibility(View.VISIBLE);
    }

    private void showTimetable() {
        mScheduleView.setVisibility(View.VISIBLE);
        mDefaultMessageView.setVisibility(View.GONE);
    }

    @Override
    public void onReceiveSuccess(@NonNull List<TimetableEntry> timetableEntries) {
        mScheduleView.setAdapter(new TimetableAdapter(context, timetableEntries));
        showTimetable();

        if (onRefreshComplete != null) {
            onRefreshComplete.onRefresh(RefreshListener.RefreshStatus.REFRESH_SUCCESS);
        }
    }

    @Override
    public void onReceiveError(@NonNull Exception ex) {
        showDefaultMessage();

        if (onRefreshComplete != null) {
            onRefreshComplete.onRefresh(RefreshListener.RefreshStatus.REFRESH_FAILURE);
        }
    }

}
