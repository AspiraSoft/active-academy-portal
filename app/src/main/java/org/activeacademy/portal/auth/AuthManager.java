package org.activeacademy.portal.auth;


import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthManager implements Runnable {

    private final Activity activity;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private AuthenticationListener listener;

    public AuthManager(Activity activity) {
        this.activity = activity;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public void setAuthenticationListener(AuthenticationListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        if (currentUser == null) {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                currentUser = mAuth.getCurrentUser();
                                if (listener != null) {
                                    listener.onSignInSuccess(currentUser);
                                }
                            } else {
                                if (listener != null) {
                                    listener.onSignInFailure();
                                }
                            }
                        }
                    });
        } else if (listener != null) {
            listener.onSignInSuccess(currentUser);
        }
    }

}
