package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.ui.messages.AllMessagesActivity;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;
/**
 * Created by Prajakta Patil on 24/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}