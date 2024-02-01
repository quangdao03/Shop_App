package com.example.shop_app.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class Utility {
    public static void toast(Context context, String msg, int length) {
        Toast.makeText(context, msg, length).show();
    }

    public static void toast(Fragment fragment, String msg, int length) {
        Context context = fragment.getContext();
        if (context != null) {
            toast(context, msg, length);
        }
    }

    public static void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    public static void hideInvisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    public static void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

}
