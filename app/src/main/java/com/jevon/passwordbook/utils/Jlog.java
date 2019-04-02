package com.jevon.passwordbook.utils;

import android.util.Log;

public class Jlog {
    public static final String TAG = "Mr.J";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void d(int msg) {
        Log.d(TAG, msg + "");
    }

    public static void d(long msg) {
        Log.d(TAG, msg + "");
    }
}