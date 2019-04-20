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
        editor = sharedPreferences.edit();
    }


    public void putPassword(String password) {
        editor.putString("password", password);
        editor.commit();
    }

    public void putStatus(boolean statu) {
        editor.putBoolean("status", statu);
        editor.commit();
    }

    public String getPassword() {
        return sharedPreferences.getString("password", "");
    }

    public boolean getStatus(String password) {
        return sharedPreferences.getBoolean("status", false);
    }
}
