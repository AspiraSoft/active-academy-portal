package org.activeacademy.portal.db;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.activeacademy.portal.auth.LoginManager;
import org.activeacademy.portal.utils.ResponseHandler;

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

    private void getAsync(String remotePath, ValueEventListener receiver) {
        final FirebaseUser currentUser = mLoginManager.getCurrentUser();
        if (mLoginManager.isUserSignedIn() && currentUser != null) {
            DatabaseReference reference = mDatabase.getReference(remotePath);
            reference.addListenerForSingleValueEvent(receiver);
        } else {
            receiver.onCancelled(null);
        }
    }

    public <T> void getAsync(String remotePath, final Class<T> responseType,
                             final ResponseHandler<T> responseHandler) {
        getAsync(remotePath, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    T response = dataSnapshot.getValue(responseType);
                    if (response == null) {
                        throw new NullPointerException();
                    }

                    responseHandler.onReceiveSuccess(response);
                } catch (ClassCastException | NullPointerException ex) {
                    responseHandler.onReceiveError(ex);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public <T> void getAsync(String remotePath, final GenericTypeIndicator<T> responseType,
                             final ResponseHandler<T> responseHandler) {
        getAsync(remotePath, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    T response = dataSnapshot.getValue(responseType);
                    if (response == null) {
                        throw new NullPointerException();
                    }

                    responseHandler.onReceiveSuccess(response);
                } catch (ClassCastException | NullPointerException ex) {
                    responseHandler.onReceiveError(ex);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}