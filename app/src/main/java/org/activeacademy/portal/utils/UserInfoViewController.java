package org.activeacademy.portal.utils;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import org.activeacademy.portal.DashboardActivity;
import org.activeacademy.portal.R;
import org.activeacademy.portal.models.Instructor;

public class UserInfoViewController implements ResponseHandler<Instructor> {

    private RefreshListener onRefreshComplete;

    private final View mLoadingView;
    private final View[] mContentViews;
    private final TextView mErrorView;

    private final TextView mNameView;
    private final TextView mPhoneView;
    private final TextView mEmailView;
    private final TextView mPositionView;
    private final TextView mJoinDateView;
    private final TextView mSalaryView;

    public UserInfoViewController(DashboardActivity activity) {
        mLoadingView = activity.findViewById(R.id.dashboard_loading);
        mErrorView = activity.findViewById(R.id.dashboard_error);

        mNameView = activity.findViewById(R.id.name);
        mPhoneView = activity.findViewById(R.id.phone);
        mEmailView = activity.findViewById(R.id.email);
        mPositionView = activity.findViewById(R.id.position);
        mJoinDateView = activity.findViewById(R.id.join_date);
        mSalaryView = activity.findViewById(R.id.salary);

        mContentViews = new View[]{
                activity.findViewById(R.id.dashboard_row1),
                activity.findViewById(R.id.dashboard_row2),
                activity.findViewById(R.id.dashboard_row3)
        };
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
        for (View v : mContentViews) {
            v.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setLoading(boolean loading) {
        setContentVisible(!loading);
        setLoaderVisible(loading);
        setErrorVisible(false);
    }

    public void showError(String error) {
        setContentVisible(false);
        setLoaderVisible(false);
        setErrorVisible(true);

        mErrorView.setText(error);
    }

    @Override
    public void onReceiveSuccess(@NonNull Instructor instructor) {
        mNameView.setText(instructor.getName());
        mPhoneView.setText(instructor.getPhone());
        mEmailView.setText(instructor.getEmail());
        mPositionView.setText(instructor.getPosition());
        mJoinDateView.setText("since " + instructor.getJoinDate());
        mSalaryView.setText("at PKR " + String.valueOf(instructor.getSalary()) + "/-");

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
