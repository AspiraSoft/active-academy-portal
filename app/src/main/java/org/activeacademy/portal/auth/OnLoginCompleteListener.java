package org.activeacademy.portal.auth;

import com.google.firebase.auth.FirebaseUser;

public interface OnLoginCompleteListener {

    void onAnonymousLoggedIn();

    void onUserLoggedIn(FirebaseUser user);

    void onLoginFailure();

}