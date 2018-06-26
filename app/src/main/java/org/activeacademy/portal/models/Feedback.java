package org.activeacademy.portal.models;

import android.app.Activity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;


public class Feedback {

    private String sender;
    private String content;
    private Float rating;

    public Feedback() {
        // Default constructor required for calls to DataSnapshot.getValue(Feedback.class)
    }

    public Feedback(String sender, String content, Float rating) {
        this.sender = sender;
        this.content = content;
        this.rating = rating;
    }

    public Float getRating() {
        return rating;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public static void submit(Activity activity, Feedback feedback, OnSuccessListener<Void> listener) {
        FirebaseDatabase.getInstance()
                .getReference("feedback").push()
                .setValue(feedback)
                .addOnSuccessListener(activity, listener);
    }

}
