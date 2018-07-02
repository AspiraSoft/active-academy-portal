package org.activeacademy.portal.utils;

/**
 * @author saifkhichi96
 * @version 1.0.0
 * @since 1.0.0 02/07/2018 6:58 PM
 */

public interface RefreshListener {

    enum RefreshStatus {
        REFRESH_SUCCESS,
        REFRESH_FAILURE
    }

    void onRefresh(RefreshStatus status);

}
