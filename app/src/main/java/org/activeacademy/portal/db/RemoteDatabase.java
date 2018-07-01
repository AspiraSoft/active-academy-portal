package org.activeacademy.portal.db;


import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.activeacademy.portal.auth.LoginManager;
import org.activeacademy.portal.auth.OnLoginCompleteListener;
import org.activeacademy.portal.models.RemoteObject;
import org.activeacademy.portal.utils.OnRemoteObjectReceivedListener;

public class RemoteDatabase implements OnLoginCompleteListener {

    private final FirebaseDatabase mDatabase;

    private boolean hasAccess;
    private boolean isAuthenticated;

    public RemoteDatabase() {
        this.mDatabase = FirebaseDatabase.getInstance();
        this.hasAccess = false;
        this.isAuthenticated = false;
    }

    @Override
    public void onAnonymousLoggedIn() {
        hasAccess = true;
        isAuthenticated = false;
    }

    @Override
    public void onUserLoggedIn(FirebaseUser user) {
        hasAccess = true;
        isAuthenticated = true;
    }

    @Override
    public void onLoginFailure() {
        hasAccess = false;
        isAuthenticated = false;
    }

    public void getRemoteObject(@NonNull final Class<? extends RemoteObject> type,
                                @NonNull final OnRemoteObjectReceivedListener listener) {
        FirebaseUser currentUser = LoginManager.getInstance().getCurrentUser();
        if (hasAccess && isAuthenticated && currentUser != null) {
            DatabaseReference reference = mDatabase.getReference("users/" + currentUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        RemoteObject object = dataSnapshot.getValue(type);
                        if (object != null) {
                            listener.onReceiveSuccess(object);
                        } else {
                            listener.onReceiveError();
                        }
                    } catch (Exception ex) {
                        listener.onReceiveError();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onReceiveError();
                }
            });
        } else {
            listener.onReceiveError();
        }
    }

}