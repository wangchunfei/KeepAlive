package com.wcf.keepalive;

import android.app.Application;

/**
 * @author wcf
 * @time 2017/7/25 9:22
 * @desc
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized MyApplication getAppContext() {
        return instance;
    }
}
