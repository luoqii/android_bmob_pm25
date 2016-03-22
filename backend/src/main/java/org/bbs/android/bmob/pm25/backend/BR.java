package org.bbs.android.bmob.pm25.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * Created by bysong on 16-3-23.
 */
public class BR extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)
                && App.sInstance.getPref().getBoolean(App.PREF_AUTO_START_AT_BOOT, false)) {
            String mac = App.sInstance.getPref().getString(App.PREF_MAC, "");
            if (!TextUtils.isEmpty(mac)) {
                UploadService.start(context, mac);
            }
        }
    }
}
