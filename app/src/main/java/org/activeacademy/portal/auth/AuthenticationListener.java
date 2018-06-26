package org.activeacademy.portal.auth;

import com.google.firebase.auth.FirebaseUser;


public interface AuthenticationListener {

    void onSignInSuccess(FirebaseUser user);

    void onSignInFailure();

}
