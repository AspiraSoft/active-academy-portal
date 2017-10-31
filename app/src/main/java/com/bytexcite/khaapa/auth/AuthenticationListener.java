package com.bytexcite.khaapa.auth;

import com.google.firebase.auth.FirebaseUser;


public interface AuthenticationListener {

    void onSignInSuccess(FirebaseUser user);

    void onSignInFailure();

}
