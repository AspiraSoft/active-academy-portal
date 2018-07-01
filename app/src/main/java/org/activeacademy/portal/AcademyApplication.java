package org.activeacademy.portal;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * @author saifkhichi96
 * @version 1.0
 *          created on 01/07/2018 7:08 PM
 */

public class AcademyApplication extends Application {

    public static final String TAG = "AcademyApplication";

    private static SharedPreferences mSharedPreferences;

    public static SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
    }

}