package org.activeacademy.portal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import org.activeacademy.portal.models.Feedback;

public class FeedbackActivity extends AppCompatActivity {

    private EditText emailView;
    private EditText feedbackView;

    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        emailView = (EditText) findViewById(R.id.email);
        feedbackView = (EditText) findViewById(R.id.message);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(2.5f);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailView.getText().toString().replace("\n", "").trim();
                String feedback = feedbackView.getText().toString().replace("\n", "").trim();;

                if (!email.endsWith("@seecs.edu.pk")) {
                    Toast.makeText(FeedbackActivity.this, "Invalid email address.", Toast.LENGTH_SHORT).show();
                }

                else if (email.equals("") || feedback.equals("")) {
                    Toast.makeText(FeedbackActivity.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                }

                else {
                    Feedback.submit(FeedbackActivity.this, new Feedback(email, feedback, ratingBar.getRating()), new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(FeedbackActivity.this, "Feedback submitted.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
    }
}
