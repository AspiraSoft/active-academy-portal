package org.activeacademy.portal.auth;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginManager implements AuthenticationListener, ValueEventListener {

    private final AuthManager mAuth;

    private final FirebaseDatabase cloudDb;

    private DatabaseReference reference;

    private OnLoginCompleteListener onLoginCompleteListener;
    private String password;

    public LoginManager(Activity activity) {
        cloudDb = FirebaseDatabase.getInstance();
        try {
            cloudDb.setPersistenceEnabled(true);
        } catch (DatabaseException ex) {
            ex.printStackTrace();
        }

        this.mAuth = new AuthManager(activity);
        this.mAuth.setAuthenticationListener(this);
    }

    public void singIn(String username, String password, OnLoginCompleteListener onLoginCompleteListener) {
        this.reference = cloudDb.getReference("accounts/" + username);
        this.password = password;

        this.onLoginCompleteListener = onLoginCompleteListener;
        this.mAuth.run();
    }

    @Override
    public void onSignInSuccess(FirebaseUser user) {
        reference.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onSignInFailure() {

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        try {
            if (dataSnapshot.getValue() == null || !dataSnapshot.getValue().equals(password)) {
                onLoginCompleteListener.onLoginFailure();
            } else {
                onLoginCompleteListener.onLoginSuccess();
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.e("ACTIVE", databaseError.getDetails());
        Log.e("ACTIVE", databaseError.getMessage());
    }

    public static void endSession(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("ACTIVE_PREFS", Context.MODE_PRIVATE);
        prefs.edit().putString("activeSession", null).apply();
    }

    public static boolean isSessionActive(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("ACTIVE_PREFS", Context.MODE_PRIVATE);
        return prefs.getString("activeSession", null) != null;
    }

    public static void saveSession(Activity activity, String username) {
        SharedPreferences prefs = activity.getSharedPreferences("ACTIVE_PREFS", Context.MODE_PRIVATE);
        prefs.edit().putString("activeSession", username).apply();
    }

    public static String activeUser(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("ACTIVE_PREFS", Context.MODE_PRIVATE);
        return prefs.getString("activeSession", "");
    }
}