package org.bbs.android.bmob.pm25.backend;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.bbs.android.pm25.library.PMS50003;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by bysong on 16-3-23.
 */
public class UploadService extends Service {

    private static final String TAG = UploadService.class.getSimpleName();
    private static final int ONGOING_NOTIFICATION_ID = R.layout.activity_main;
    private final IBinder mLocalBinder = new LocalBinder();
    private Uploader mUploader;

    // FIXME why 2time for this method >>>
    public UploadService(){
        Log.d(TAG, "UploadService." + this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mUploader = Uploader.getInstance(getApplication());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        parseIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    void parseIntent(Intent intent) {
        String mac = intent.getStringExtra(BtDeviceActivity.EXTRA_MAC);
        if (!TextUtils.isEmpty(mac)) {
            startService(mac);
        }
    }

    public void startService(String mac) {
        mUploader.startService(mac);
    }

    public void stopSerivce() {
        mUploader.stopSerivce();
    }

    public String getStatistics() {
        return mUploader.getStatistics();
    }

    public String getStatus() {
        return mUploader.getStatus();
    }

    public static void start(Context context, String mac) {
        Intent self = new Intent(context, UploadService.class);
        self.putExtra(BtDeviceActivity.EXTRA_MAC, mac);
        context.startService(self);
    }

    public class LocalBinder extends Binder {
        UploadService getService() {
            // Return this instance of LocalService so clients can call public methods
            return UploadService.this;
        }
    }


}
