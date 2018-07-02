package org.activeacademy.portal.utils;

import android.support.annotation.NonNull;

import org.activeacademy.portal.models.RemoteObject;

import java.util.List;

/**
 * @author saifkhichi96
 * @version 1.0
 *          created on 01/07/2018 5:52 PM
 */

public interface OnRemoteObjectsReceivedListener<T extends RemoteObject> {

    void onReceiveSuccess(@NonNull List<T> tList);

    void onReceiveError();

}