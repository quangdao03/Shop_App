package com.example.shop_app.utils;

import android.content.Context;

public class DatalocalManager {
    private static final String PREF_FIRST = "PREF_FIRST";
    private static final String PREF_FIRST2 = "PREF_FIRST2";
    private static final String PREF_FIRST3 = "PREF_FIRST3";

    public static DatalocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context){
        instance = new DatalocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DatalocalManager getInstance(){
        if (instance == null){
            instance = new DatalocalManager();
        }
        return instance;
    }

    public static void setFirstInstalled(boolean isFirst){
        DatalocalManager.getInstance().mySharedPreferences.putBooleanValue(PREF_FIRST,isFirst);
    }

    public static void setFirstInstalled2(boolean isFirst2){
        DatalocalManager.getInstance().mySharedPreferences.putBooleanValue(PREF_FIRST2,isFirst2);
    }


    public static Boolean getFirstInstalled(){
       return DatalocalManager.getInstance().mySharedPreferences.getBooleanValue(PREF_FIRST);
    }
    public static Boolean getFirstInstalled2(){
        return DatalocalManager.getInstance().mySharedPreferences.getBooleanValue2(PREF_FIRST2);
    }

}
