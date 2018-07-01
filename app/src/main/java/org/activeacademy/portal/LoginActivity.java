package org.activeacademy.portal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import org.activeacademy.portal.auth.LoginManager;
import org.activeacademy.portal.auth.OnLoginCompleteListener;
import org.activeacademy.portal.db.LocalDatabase;
import org.activeacademy.portal.view.CustomButton;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener, OnLoginCompleteListener {

    private LoginManager mLoginManager;

    private EditText mEmailField;
    private EditText mPasswordField;
    private AppCompatCheckBox mRememberField;
    private CustomButton mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get UI references
        mEmailField = findViewById(R.id.emailField);
        mPasswordField = findViewById(R.id.passwordField);
        mRememberField = findViewById(R.id.rememberUser);

        // Restore any saved credentials
        String[] savedCredentials = LocalDatabase.getInstance().getSavedCredentials();
        if (savedCredentials != null && savedCredentials.length >= 2) {
            mEmailField.setText(savedCredentials[0]);
            mPasswordField.setText(savedCredentials[1]);
            mRememberField.setChecked(true);
        } else {
            mRememberField.setChecked(false);
        }

        // Set event listeners
        mLoginButton = findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(this);

        mLoginManager = LoginManager.getInstance();
    }

    private boolean isEmailValid(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return !email.isEmpty() && email.matches(emailPattern);
    }

    private boolean isPasswordValid(String password) {
        return !password.trim().isEmpty();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                // Get strings from input fields
                final String email = mEmailField.getText().toString().trim();
                final String password = mPasswordField.getText().toString().trim();

                // Validate input
                if (!isEmailValid(email) || !isPasswordValid(password)) {
                    Toast.makeText(this, "Invalid input. Check your credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save credentials if user said so
                if (mRememberField.isChecked()) {
                    LocalDatabase.getInstance().saveCredentials(email, password);
                } else {
                    LocalDatabase.getInstance().removeSavedCredentials();
                }

                // Disable sign in button
                mLoginButton.setEnabled(false);

                // Start asynchronous sign in operation
                Toast.makeText(LoginActivity.this, "Signing in ...", Toast.LENGTH_SHORT).show();
                mLoginManager.signInWithCredentials(email, password, this);
                break;
        }
    }

    @Override
    public void onUserLoggedIn(FirebaseUser user) {
        mLoginButton.setEnabled(true);
        Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();

        // Open Dashboard
        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        finish();
    }

    @Override
    public void onLoginFailure() {
        mLoginButton.setEnabled(true);
        Toast.makeText(LoginActivity.this, "Login failed. Check your credentials", Toast.LENGTH_SHORT).show();
    }

}