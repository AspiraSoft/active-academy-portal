package org.activeacademy.portal.utils;

import android.support.annotation.NonNull;

/**
 * @author saifkhichi96
 * @version 1.0
 *          created on 01/07/2018 5:52 PM
 */

public interface ResponseHandler<T> {

    void onReceiveSuccess(@NonNull T t);

    void onReceiveError(@NonNull Exception ex);

}