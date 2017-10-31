package com.bytexcite.khaapa.db;


import android.app.Activity;

import com.bytexcite.khaapa.auth.AuthManager;
import com.bytexcite.khaapa.auth.AuthenticationListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DataListener implements AuthenticationListener, ValueEventListener {

    private final AuthManager mAuth;

    private final LocalDatabase localDb;
    private final DatabaseReference cloudDb;

    private OnDataSynchronizedListener onDataSynchronizedListener;

    public DataListener(Activity activity) {
        FirebaseDatabase cloudDatabase = FirebaseDatabase.getInstance();
        cloudDatabase.setPersistenceEnabled(true);

        this.mAuth = new AuthManager(activity);
        this.mAuth.setAuthenticationListener(this);

        this.cloudDb = cloudDatabase.getReference("items");
        this.localDb = LocalDatabase.getInstance();
    }

    public void register(OnDataSynchronizedListener synchronizedListener) {
        this.onDataSynchronizedListener = synchronizedListener;
        this.mAuth.run();
    }

    @Override
    public void onSignInSuccess(FirebaseUser user) {
        cloudDb.addValueEventListener(this);
    }

    @Override
    public void onSignInFailure() {

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        try {
            HashMap<String, HashMap> items = (HashMap<String, HashMap>) dataSnapshot.getValue();
            try {
                localDb.syncFoods(items.get("food"));
            } catch (Exception ignored) {

            }
            try {
                localDb.syncDrinks(items.get("drinks"));
            } catch (Exception ignored) {

            }
            try {
                localDb.syncOtherItems(items.get("others"));
            } catch (Exception ignored) {

            }
            onDataSynchronizedListener.onDataSynchronized();
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}