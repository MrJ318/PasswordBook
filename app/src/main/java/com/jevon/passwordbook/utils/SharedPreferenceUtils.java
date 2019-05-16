package com.jevon.passwordbook.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/4/13 15:42
 */
public class SharedPreferenceUtils {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferenceUtils(Context context) {
        sharedPreferences = context.getSharedPreferences("passwordbook", Context.MODE_PRIVATE);
    }

    public String getPassword() {
        return sharedPreferences.getString("password", "");
    }

    public void putPassword(String password) {
        editor = sharedPreferences.edit();
        editor.putString("password", password);
        editor.apply();
    }

    public boolean getStatus() {
        return sharedPreferences.getBoolean("status", false);
    }

    public void putStatus(boolean status) {
        editor = sharedPreferences.edit();
        editor.putBoolean("status", status);
        editor.apply();
    }
}
