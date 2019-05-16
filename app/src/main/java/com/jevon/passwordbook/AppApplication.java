package com.jevon.passwordbook;

import android.app.Application;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/4/2 12:02
 */
public class AppApplication extends Application {

    private static AppApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static AppApplication getInstance() {
        return app;
    }

}
