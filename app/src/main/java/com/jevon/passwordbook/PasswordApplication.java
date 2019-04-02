package com.jevon.passwordbook;

import android.app.Application;
import android.content.Context;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/4/2 12:02
 */
public class PasswordApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        this.context = getApplicationContext();
        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }
}
