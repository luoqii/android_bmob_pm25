package org.bbs.android.bmob.pm25;

import android.app.Application;

import cn.bmob.v3.Bmob;
import io.realm.RealmConfiguration;

/**
 * Created by bysong on 16-3-17.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Bmob.DEBUG = true;
        // bangbang.s
        Bmob.initialize(this, "5543fd2c5165876ee945a6e2dae71718");

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
    }
}
