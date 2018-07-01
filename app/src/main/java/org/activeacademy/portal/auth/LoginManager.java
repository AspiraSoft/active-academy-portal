package org.activeacademy.portal.auth;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginManager {

    private static LoginManager ourInstance;

    private final FirebaseAuth mAuth;

    private FirebaseUser currentUser = null;

    private LoginManager() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public static LoginManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new LoginManager();
        }

        return ourInstance;
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public void signInWithCredentials(@NonNull String email, @NonNull String password,
                                      @Nullable final OnLoginCompleteListener onLoginCompleteListener) {
        if (isUserSignedIn()) {
            onSignInComplete(currentUser, onLoginCompleteListener);
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                onSignInComplete(mAuth.getCurrentUser(), onLoginCompleteListener);
                            } else {
                                onSignInComplete(null, onLoginCompleteListener);
                            }
                        }
                    });
        }
    }

    public void signOut() {
        mAuth.signOut();
    }

    public boolean isUserSignedIn() {
        return currentUser != null && !currentUser.isAnonymous();
    }

    private void onSignInComplete(@Nullable FirebaseUser updatedUser,
                                  @Nullable OnLoginCompleteListener listener) {
        currentUser = updatedUser;
        if (listener != null) {
            if (updatedUser == null || updatedUser.isAnonymous()) {
                listener.onLoginFailure();
            } else {
                listener.onUserLoggedIn(updatedUser);
            }
        }
    }

}