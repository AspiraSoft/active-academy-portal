package org.activeacademy.portal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import org.activeacademy.portal.auth.LoginManager;
import org.activeacademy.portal.errors.UnknownException;
import org.activeacademy.portal.view.CustomButton;

public class PasswordActivity extends AppCompatActivity implements
        View.OnClickListener, OnCompleteListener<Void> {

    private EditText mEmailField;
    private CustomButton mResetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        // Get UI references
        mEmailField = findViewById(R.id.emailField);
        mResetButton = findViewById(R.id.resetButton);

        // Set event listeners
        mResetButton.setOnClickListener(this);
    }

    private boolean isEmailValid(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return !email.isEmpty() && email.matches(emailPattern);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetButton:
                // Get strings from input fields
                final String email = mEmailField.getText().toString().trim();

                // Validate input
                if (!isEmailValid(email)) {
                    Toast.makeText(this, "Invalid input. Check your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Disable reset button
                mResetButton.setEnabled(false);

                // Start asynchronous reset operation
                Toast.makeText(this, "Initiating request ...", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().resetPassword(email, this);
                break;
        }
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        // Enable reset button
        mResetButton.setEnabled(true);

        if (task.isSuccessful()) {
            Snackbar.make(mEmailField, "You will receive an email shortly.", Snackbar.LENGTH_LONG).show();
        } else {
            Exception exception = task.getException();
            if (exception == null) {
                exception = new UnknownException();
            }

            try {
                throw exception;
            } catch (FirebaseNetworkException e) {
                Toast.makeText(this, "Check your network connection", Toast.LENGTH_LONG).show();
            } catch (FirebaseAuthInvalidUserException e) {
                Toast.makeText(this, "No such user exists", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

}