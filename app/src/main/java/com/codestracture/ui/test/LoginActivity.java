package com.codestracture.ui.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.codestracture.R;
import com.codestracture.data.manager.preference.PreferenceManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Demo Login activity class
 */
@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    @Inject
    PreferenceManager preferenceManager;

    private static final String DEMO_EMAIL = "espresso@spoon.com";
    private static final String DEMO_PASSWORD = "lemoncake";

    private EditText emailEditText, passwordEditText;
    private View errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        View submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                validateFields();

                if (emailEditText.getError() == null && passwordEditText.getError() == null) {
                    validateAccount();
                }
            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }

    private void validateFields() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
            emailEditText.setError(getString(R.string.msg_email_error));
        } else {
            emailEditText.setError(null);
        }

        if (passwordEditText.getText().toString().isEmpty()) {
            passwordEditText.setError(getString(R.string.msg_password_error));
        } else {
            passwordEditText.setError(null);
        }
    }

    private void validateAccount() {
        if (errorView == null) {
            errorView = findViewById(R.id.error);
        }

        if (!emailEditText.getText().toString().equals(DEMO_EMAIL) || !passwordEditText.getText().toString().equals(DEMO_PASSWORD)) {
            errorView.setVisibility(View.VISIBLE);
            preferenceManager.setLogin(false);
            Log.d("MyTag","set isLogin::"+false);
        } else {
            errorView.setVisibility(View.GONE);
            preferenceManager.setLogin(true);
            Log.d("MyTag","set isLogin::"+true);
        }
    }
}
