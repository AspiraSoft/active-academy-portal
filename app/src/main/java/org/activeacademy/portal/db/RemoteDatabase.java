package org.activeacademy.portal.db;


import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.activeacademy.portal.auth.LoginManager;
import org.activeacademy.portal.models.RemoteObject;
import org.activeacademy.portal.utils.OnRemoteObjectReceivedListener;

public class RemoteDatabase {

    private static final RemoteDatabase ourInstance = new RemoteDatabase();

    private final FirebaseDatabase mDatabase;
    private final LoginManager mLoginManager;

    private RemoteDatabase() {
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mLoginManager = LoginManager.getInstance();
    }

    public static RemoteDatabase getInstance() {
        return ourInstance;
    }

    public void getRemoteObject(@NonNull final Class<? extends RemoteObject> type,
                                @NonNull final OnRemoteObjectReceivedListener listener) {
        FirebaseUser currentUser = mLoginManager.getCurrentUser();
        if (mLoginManager.isUserSignedIn() && currentUser != null) {
            DatabaseReference reference = mDatabase.getReference("users/" + currentUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        RemoteObject object = dataSnapshot.getValue(type);
                        if (object != null) {
                            listener.onReceiveSuccess(type.cast(object));
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