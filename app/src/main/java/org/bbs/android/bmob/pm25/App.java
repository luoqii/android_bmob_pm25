package org.bbs.android.bmob.pm25;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import org.bbs.android.pm25.library.BaseApp;

import cn.bmob.v3.Bmob;
import io.realm.RealmConfiguration;

/**
 * Created by bysong on 16-3-17.
 */
public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
    }
}
