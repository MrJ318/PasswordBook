package com.jevon.passwordbook.utils;

import android.widget.Toast;

import com.jevon.passwordbook.AppApplication;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/5/8 17:13
 */
public class Jtoast {
    public static void show(String msg) {
        Toast.makeText(AppApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
