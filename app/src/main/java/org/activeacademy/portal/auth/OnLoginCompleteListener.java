package org.activeacademy.portal.auth;

import com.google.firebase.auth.FirebaseUser;

public interface OnLoginCompleteListener {

    void onUserLoggedIn(FirebaseUser user);

    void onLoginFailure();

}