package com.example.shop_app.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {
    public static final String SENDID = "idsend";
    public static final String RECEIVEDID = "idreceived";
    public static final String MESS = "message";
    public static final String DATETIME = "datatime";
    public static final String PATH_CHAT = "chat";
    public static final String IMAGE = "image";

    public static String SELLERID = "";
    public static String Name = "";
    public static String Image = "";
    public static String ImageSeller = "";

    public static String name_shop = "";

    public static String FCM_KEY = "AAAABpqTFpo:APA91bE7CQdDsYuLaaIHzYmYsrTpbEVBZcPChAD9kKdMHRCLSqxGj4Y0cVf8_gY_v7Vz_4i0PzZP4nvQrf-Uv92FpQDEqhIDUra8x6olHpuUFe-BKUB4lgm8sCBvHfiYxQKL2J7mfZce";
    public static final String FCM_TOPIC = "PUSH_NOTIFICATIONS";
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}
