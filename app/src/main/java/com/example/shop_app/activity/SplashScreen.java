package com.example.shop_app.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.shop_app.R;
import com.example.shop_app.utils.Checkconnection;
import com.example.shop_app.utils.DatalocalManager;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);


        loadData();
    }
    private  void loadData(){
         if (Checkconnection.isNetworkAvaible(this)){
             //Network connected
             if(!DatalocalManager.getFirstInstalled()) {
                 new Handler().postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         startActivity(new Intent(SplashScreen.this, OnboardingActivity.class));
                         DatalocalManager.setFirstInstalled(true);
                         finish();
                     }
                 }, 2000);
             }else if(!DatalocalManager.getFirstInstalled2()){
                 new Handler().postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                         DatalocalManager.setFirstInstalled2(true);
                         finish();
                     }
                 }, 2000);
             }else {
                 new Handler().postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                         finish();
                     }
                 }, 2000);
             }

         }else {
             Toast.makeText(this, "Network disconected", Toast.LENGTH_SHORT).show();
         }
    }
}