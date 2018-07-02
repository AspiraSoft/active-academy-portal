package org.activeacademy.portal.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.activeacademy.portal.CoursesActivity;
import org.activeacademy.portal.R;
import org.activeacademy.portal.models.CourseEntry;

import java.util.List;

public class CoursesViewController implements ResponseHandler<List<CourseEntry>> {

    private final Context context;

    private final ProgressBar mLoadingView;
    private final ListView mCoursesView;
    private final TextView mErrorView;

    private RefreshListener onRefreshComplete;

    public CoursesViewController(CoursesActivity activity) {
        this.context = activity;

        mLoadingView = activity.findViewById(R.id.courses_loading);
        mCoursesView = activity.findViewById(R.id.courses);
        mErrorView = activity.findViewById(R.id.courses_error);

        setLoading(true);
    }

    public void setOnRefreshComplete(RefreshListener onRefreshComplete) {
        this.onRefreshComplete = onRefreshComplete;
    }

    private void setLoaderVisible(boolean visible) {
        mLoadingView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void setErrorVisible(boolean visible) {
        mErrorView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    private void setContentVisible(boolean visible) {
        mCoursesView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    private void setLoading(boolean loading) {
        setContentVisible(!loading);
        setLoaderVisible(loading);
        setErrorVisible(false);
    }

    private void showError(String error) {
        setContentVisible(false);
        setLoaderVisible(false);
        setErrorVisible(true);

        mErrorView.setText(error);
    }

    public void showError() {
        showError(mErrorView.getText().toString());
    }

    @Override
    public void onReceiveSuccess(@NonNull List<CourseEntry> courseEntries) {
        mCoursesView.setAdapter(new CoursesAdapter(context, courseEntries));
        setLoading(false);

        if (onRefreshComplete != null) {
            onRefreshComplete.onRefresh(RefreshListener.RefreshStatus.REFRESH_SUCCESS);
        }
    }

    @Override
    public void onReceiveError(@NonNull Exception ex) {
        showError(ex.getMessage());

        if (onRefreshComplete != null) {
            onRefreshComplete.onRefresh(RefreshListener.RefreshStatus.REFRESH_FAILURE);
        }
    }

}