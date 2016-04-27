package org.bbs.android.pm25.library;

import android.app.Application;
import android.content.SharedPreferences;

import com.squareup.leakcanary.LeakCanary;

import cn.bmob.v3.Bmob;

/**
 * Created by bysong on 16-4-27.
 */
public class BaseApp extends Application {
    public static BaseApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        Bmob.DEBUG = true;
        // bangbang.s
        Bmob.initialize(this, "68926b81aa2f422e39fc6b08c01799b6");

        LeakCanary.install(this);
    }

    public SharedPreferences getPref(){
        return getSharedPreferences("bt_backend", 0);
    }
}
