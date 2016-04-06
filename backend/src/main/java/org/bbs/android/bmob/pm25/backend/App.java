package org.bbs.android.bmob.pm25.backend;

import android.app.Application;
import android.content.SharedPreferences;

import com.squareup.leakcanary.LeakCanary;

import cn.bmob.v3.Bmob;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static org.bbs.android.bmob.pm25.backend.BR.startService4LastTime;

/**
 * Created by bysong on 16-3-23.
 */
public class App extends Application {
    public static final String PREF_AUTO_START_AT_BOOT = "auto_start_at_boot";
    public static final String KEY_MAC = "mac";
    public static App sInstance;
    public static RealmConfiguration sRealmConfig;

    @Override
    public void onCreate() {
        super.onCreate();

        Bmob.DEBUG = true;
        // bangbang.s
        Bmob.initialize(this, "5543fd2c5165876ee945a6e2dae71718");

        sInstance = this;
        sRealmConfig = new RealmConfiguration.Builder(this).build();

//        startService4LastTime(this);
        LeakCanary.install(this);

    }

    public SharedPreferences getPref(){
        return getSharedPreferences("bt_backend", 0);
    }
}
