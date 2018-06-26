package org.activeacademy.portal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.activeacademy.portal.auth.LoginManager;
import org.activeacademy.portal.auth.OnLoginCompleteListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameView;
    private EditText passwordView;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginManager = new LoginManager(this);

        usernameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.password);
        findViewById(R.id.loginButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                final String username = usernameView.getText().toString().trim();
                final String password = passwordView.getText().toString().trim();

                if (!username.equals("") && !password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Logging in ...", Toast.LENGTH_SHORT).show();
                    loginManager.singIn(username, password, new OnLoginCompleteListener() {
                        @Override
                        public void onLoginSuccess() {
                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }

                        @Override
                        public void onLoginFailure() {
                            Toast.makeText(LoginActivity.this, "Invalid username/password combination!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    if (((CheckBox) findViewById(R.id.rememberUser)).isChecked()) {
                        LoginManager.saveSession(this, username);
                    }
                } else {
                    Toast.makeText(this, "Provide credentials!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
