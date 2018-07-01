package org.activeacademy.portal.utils;

import android.support.annotation.NonNull;

import org.activeacademy.portal.models.RemoteObject;

/**
 * @author saifkhichi96
 * @version 1.0
 *          created on 01/07/2018 5:52 PM
 */

public interface OnRemoteObjectReceivedListener {

    void onReceiveSuccess(@NonNull RemoteObject object);

    void onReceiveError();

}