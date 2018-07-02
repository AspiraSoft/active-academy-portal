package org.activeacademy.portal.db;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import org.activeacademy.portal.AcademyApplication;

/**
 * @author saifkhichi96
 * @version 1.0
 *          created on 01/07/2018 8:15 PM
 */

public class LocalDatabase {
    private static final LocalDatabase ourInstance = new LocalDatabase();

    private final SharedPreferences mPrefs;

    private LocalDatabase() {
        mPrefs = AcademyApplication.getSharedPreferences();
    }

    public static LocalDatabase getInstance() {
        return ourInstance;
    }

    public void saveCredentials(final String email, final String password) {
        mPrefs.edit()
                .putString("email", email)
                .putString("password", password)
                .apply();
    }

    @Nullable
    public String[] getSavedCredentials() {
        String email = mPrefs.getString("email", null);
        String password = mPrefs.getString("password", null);

        if (email != null && password != null) {
            return new String[]{email, password};
        } else {
            return null;
        }
    }

    public void removeSavedCredentials() {
        mPrefs.edit()
                .putString("email", null)
                .putString("password", null)
                .apply();
    }

}