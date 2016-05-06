package org.bbs.android.pm25.library;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.BuildConfig;
import android.support.multidex.MultiDex;

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
        Bmob.DEBUG = BuildConfig.DEBUG;
        // bangbang.s
        Bmob.initialize(this, "68926b81aa2f422e39fc6b08c01799b6");

        LeakCanary.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

//        MultiDex.install(this);
    }

    public SharedPreferences getPref(){
        return getSharedPreferences("bt_backend", 0);
    }
}
