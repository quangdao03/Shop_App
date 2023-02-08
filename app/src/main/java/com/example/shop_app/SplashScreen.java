package com.example.shop_app;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;


import com.example.shop_app.utils.DatalocalManager;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

    if(!DatalocalManager.getFirstInstalled()) {
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashScreen.this, OnboardingActivity.class));
            DatalocalManager.setFirstInstalled(true);
        }
    }, 2000);
    }else if(!DatalocalManager.getFirstInstalled2()){
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            DatalocalManager.setFirstInstalled2(true);
        }
    }, 2000);
    }else {
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
        }
    }, 1000);
        }
    }
}