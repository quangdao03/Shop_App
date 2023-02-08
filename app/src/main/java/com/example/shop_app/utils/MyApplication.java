package com.example.shop_app.utils;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatalocalManager.init(getApplicationContext());
    }
}
