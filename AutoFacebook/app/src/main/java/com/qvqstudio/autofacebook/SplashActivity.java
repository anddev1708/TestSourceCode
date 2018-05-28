package com.qvqstudio.autofacebook;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

public class SplashActivity extends AppCompatActivity {

    private SessionData sessionData;
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Check location
        sessionData = new SessionData(this);
        String localToken = sessionData.getObjectAsString(SessionData.TOKEN);
        if(TextUtils.isEmpty(localToken))
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goLogin();
                }
            }, SPLASH_TIME_OUT);

        else

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goMainScreen();
                }
            }, SPLASH_TIME_OUT);

    }

    private void goMainScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
