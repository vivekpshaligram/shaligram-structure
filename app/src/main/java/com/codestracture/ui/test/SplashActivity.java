package com.codestracture.ui.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.codestracture.R;
import com.codestracture.data.manager.preference.PreferenceManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {

    @Inject
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        boolean isLogin = preferenceManager.getLogin();
        Log.d("MyTag","isLogin::"+isLogin);
        Intent intent;
        if (isLogin) {
            intent = new Intent(SplashActivity.this, SignupActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }
       // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
