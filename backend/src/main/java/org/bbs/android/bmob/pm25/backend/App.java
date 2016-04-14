package org.bbs.android.bmob.pm25.backend;

import android.app.Application;
import android.content.SharedPreferences;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.squareup.leakcanary.LeakCanary;

import org.bbs.android.pm25.library.AV_PMS50003;

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

    public static String DOMAN = "http://api.yeelink.net";
    public static String PATH = "/v1.0/device/345954/sensor/386502/datapoints";
    public static String APK_KEY = "3fc5868bee420b4304b308d15b6ec111";

    @Override
    public void onCreate() {
        super.onCreate();

        Bmob.DEBUG = true;
        // bangbang.s
        Bmob.initialize(this, "5543fd2c5165876ee945a6e2dae71718");

        sInstance = this;
        sRealmConfig = new RealmConfiguration.Builder(this).build();

        // do this BEFORE init
        AVObject.registerSubclass(AV_PMS50003.class);
        // github bangbang.s
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "l9SCds6HwQ3cAY2nT0p4SkUU-gzGzoHsz", "XzFCHOE21vAJ4mX991cneb3u");

//        startService4LastTime(this);
        LeakCanary.install(this);
    }

    public SharedPreferences getPref(){
        return getSharedPreferences("bt_backend", 0);
    }
}
