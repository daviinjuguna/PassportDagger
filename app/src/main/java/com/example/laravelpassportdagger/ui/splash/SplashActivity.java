package com.example.laravelpassportdagger.ui.splash;

import android.content.Intent;
import android.os.Bundle;


import com.example.laravelpassportdagger.R;
import com.example.laravelpassportdagger.baseviews.BaseActivity;
import com.example.laravelpassportdagger.ui.login.LoginActivity;

public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";

    private  int SPLASH_RUNTIME = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //threads faster
        Thread splash = new Thread(){
            public void run(){
                try {
                    sleep(SPLASH_RUNTIME);
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    finish();
                }

            }
        };
        splash.start();
    }
}