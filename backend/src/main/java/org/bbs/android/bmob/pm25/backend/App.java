package org.bbs.android.bmob.pm25.backend;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

import org.bbs.android.pm25.library.AV_PMS50003;
import org.bbs.android.pm25.library.BaseApp;

import io.realm.RealmConfiguration;

/**
 * Created by bysong on 16-3-23.
 */
public class App extends BaseApp {
    public static final String PREF_AUTO_START_AT_BOOT = "auto_start_at_boot";
    public static final String KEY_MAC = "mac";

    public static final String CARRIOTS_DOMAN = "http://api.carriots.com";
    public static final String CARRIOTS_PATH = "/api/v1.6/variables/5711e45f76254237ac917c5b/values/";
    public static final String CARRIOTS_TOKEN = "QWhAbRs5G1PefcjEzP5A1kVNI53sls";

    public static final String LEWEI50_DOMAN = "http://www.lewei50.com";
    public static /*final*/ String LEWEI50_PATH = "/api/V1/gateway/UpdateSensors/01";
    public static final String LEWEI50_APK_KEY = "f4325d957ae2490d9376b65869dd90ee";
    public static final String LEWEI50_HEADER_U_APP_KEY = "userkey";

    public static final String ONENET_DOMAN = "http://api.heclouds.com";
    public static String ONENET_PATH = "/devices/1086201/datapoints";
    public static final String ONENET_APP_KEY = "DmeFcOmio4W38ocK5uT=TN02qi4=";
    public static final String ONENET_HEADER_API_KEY = "api-key";

    public static final String UBIDOTS_DOMAN = "http://things.ubidots.com";
    public static String UBIDOTS_PATH = "/api/v1.6/variables/57189ea57625420ae1393a28/values/";
    public static final String UBIDOTS_TOKEN = "QWhAbRs5G1PefcjEzP5A1kVNI53sls";

    public static final String WSN_CLOUD_DOMAN = "http://api.wsncloud.com";
    public static final String WSN_CLOUD_PATH = "/data/v1/numerical/insert";
    public static final String WSN_CLOUD_APP_KEY = "2155b71b39a381dde8ee5f8ac281d79a";
    public static String WSN_CLOUD_SENSOR_ID = "5710511de4b00415c43dd64e";

    public static String YEELINK_DOMAN = "http://api.yeelink.net";
    public static String YEELINK_PATH = "/v1.0/device/345954/sensor/386502/datapoints";
    public static String YEELINK_APK_KEY = "3fc5868bee420b4304b308d15b6ec111";
    public static String YEELINK_HEADER_U_APK_KEY = "U-ApiKey";

    public static RealmConfiguration sRealmConfig;

    @Override
    public void onCreate() {
        super.onCreate();

        sRealmConfig = new RealmConfiguration.Builder(this).build();

        // do this BEFORE init
        AVObject.registerSubclass(AV_PMS50003.class);
        //https://leancloud.cn/
        // github bangbang.s
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "fMWQQNaIbD88mHclI3PA19rj-gzGzoHsz", "lTklHlOL2TXqtf00AAgS7VXU");

//        startService4LastTime(this);
    }

}
