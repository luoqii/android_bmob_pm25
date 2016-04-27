package org.bbs.android.bmob.pm25.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.bbs.android.pm25.library.BaseApp;

/**
 * Created by bysong on 16-3-23.
 */
public class BR extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)
                && BaseApp.sInstance.getPref().getBoolean(App.PREF_AUTO_START_AT_BOOT, false)) {
            startService4LastTime(context);
        }
    }

    public static void startService4LastTime(Context context) {
        String mac = BaseApp.sInstance.getPref().getString(App.KEY_MAC, "");
        if (!TextUtils.isEmpty(mac)) {
            UploadService.start(context, mac);
        }
    }
}
