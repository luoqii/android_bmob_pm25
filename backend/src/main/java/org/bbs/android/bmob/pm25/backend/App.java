package org.bbs.android.bmob.pm25.backend;

import android.app.Application;
import android.content.SharedPreferences;

import cn.bmob.v3.Bmob;

/**
 * Created by bysong on 16-3-23.
 */
public class App extends Application {
    public static final String PREF_AUTO_START_AT_BOOT = "auto_start_at_boot";
    public static final String PREF_MAC = "mac";
    public static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();


        Bmob.DEBUG = true;
        // bangbang.s
        Bmob.initialize(this, "5543fd2c5165876ee945a6e2dae71718");

        sInstance = this;
    }

    public SharedPreferences getPref(){
        return getSharedPreferences("bt_backend", 0);
    }
}
