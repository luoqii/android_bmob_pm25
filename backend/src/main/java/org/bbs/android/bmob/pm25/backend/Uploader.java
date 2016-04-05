package org.bbs.android.bmob.pm25.backend;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import org.bbs.android.pm25.library.PMS50003;
import org.bbs.android.pm25.library.Realm_PMS50003;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by bysong on 16-3-23.
 */
public class Uploader{

    private static final String TAG = Uploader.class.getSimpleName();
    private static final int ONGOING_NOTIFICATION_ID = R.layout.activity_main;
    private static Uploader sInstance;

    private final Application mApp;
    private final PmCollector.PmCallback mSender;
    private BtThread mThread;

    private AtomicInteger mTotalAckPm;
    private AtomicInteger mTotalPm;
    private int mTotal;
    private int mTotalAck;
    private Handler mUiHandler;

    private String mStatus = "init ...";
    private UploaderThreadByRealm mUploaderThread;

    public static Uploader getInstance(Application app){
        if (null == sInstance){
            sInstance = new Uploader(app);
        }

        return sInstance;
    }

    public Uploader(Application app){
        Log.d(TAG, "UploadService." + this);
        mApp = app;

        mTotalAckPm = new AtomicInteger();
        mTotalPm = new AtomicInteger();
        mUiHandler = new Handler();
        mSender = new PmCollector.PmCallback() {
            @Override
            public void onPmAvailable(final PMS50003 pm) {
//                    mTotalPm.set(mTotalPm.get());
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTotalPm.set(mTotalPm.get() + 1);
                        mTotal++;
                        Log.d(TAG, "upload:" + mTotal + " success:" + mTotalAck + "\npm:" + pm);
                    }
                });

                pm.save(mApp, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess");
//                            mTotalAckPm.set(mTotalAckPm.get());
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mTotalAckPm.set(mTotalAckPm.get() + 1);
                                mTotalAck++;
                                mStatus = pm.pm2_5+ "";
                            }
                        });
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        mStatus = "upload error";
                        Log.w(TAG, "onFailure. i:" + i + " s:" + s);

                        Realm realm = Realm.getInstance(App.sRealmConfig);
                        realm.beginTransaction();
                        realm.copyToRealm(Realm_PMS50003.fromPm(pm));

                        realm.commitTransaction();
                        Log.w(TAG, "save by ream. pm:" + pm);
                    }
                });
            }
        };
    }

    void parseIntent(Intent intent) {
        String mac = intent.getStringExtra(BtDeviceActivity.EXTRA_MAC);
        if (!TextUtils.isEmpty(mac)) {
//            startService(mac);
        }
    }

    public void startService(String mac) {
        Log.d(TAG, "startService. mac:" + mac);
        App.sInstance.getPref().edit()
                .putString(App.KEY_MAC, mac)
                .commit();

        if (null != mThread) {
            mThread.quit();
            mThread = null;
        }
        if (null == mThread) {
            mThread = new BtThread(mac, mSender);
            mThread.start();

            mStatus = "start thread.";
        }

        if (mUploaderThread == null){
            mUploaderThread = new UploaderThreadByRealm();
            mUploaderThread.start();
        }
    }

    public void stopSerivce() {
        Log.d(TAG, "stopService.");

        if (null != mThread) {
            mThread.quit();
            mThread = null;

//            stopForeground(true);
        }

        if (null != mUploaderThread){
            mUploaderThread.quit();
        }
    }

    public String getStatistics() {
//        return mTotalAckPm.get() + "/" + mTotalPm.get();
        return (null == mThread ) ? "" : mThread.getStatistics();
    }

    public String getStatus() {
        return mStatus;
    }

    public class LocalBinder extends Binder {
        Uploader getService() {
            // Return this instance of LocalService so clients can call public methods
            return Uploader.this;
        }
    }

    class UploaderThreadByRealm extends Thread {
        private boolean mShouldQuit;
        private static final int SLEEP_MIN_TIME = 5 * 1000;
        private static final int SLEEP_MAX_TIME = 1 * 60 * 60 * 1000;
        private int mSleepTime;

        UploaderThreadByRealm () {
            mSleepTime = SLEEP_MIN_TIME;
        }

        public void quit(){
            mShouldQuit = true;
        }

        @Override
        public synchronized void start() {
            super.start();
        }

        @Override
        public void run() {
            super.run();
            while (!mShouldQuit) {
                Realm realm = Realm.getInstance(App.sRealmConfig);
                realm.refresh();
                final  RealmResults<Realm_PMS50003> result = realm.where(Realm_PMS50003.class)
                        .equalTo("hasUploaded", false)
                        .findAll();
                Log.d(TAG, "has " + result.size() + " un uploaded realm object.");

                int c = 50;
                if (result.size() < c){
                    c = result.size();
                }

                if (c != 0) {
                    mSleepTime = SLEEP_MIN_TIME;

                    final int COUNT = c;
                    final List<BmobObject> pms = new ArrayList<>();
                    for (int i = 0; i < COUNT; i++) {
                        pms.add(PMS50003.fromRealm(result.get(i)));
                    }
                    new BmobObject().insertBatch(mApp, pms, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "upload batch success. ");
                            Realm realm = Realm.getInstance(App.sRealmConfig);
                            realm.refresh();
                            realm.beginTransaction();
                            for (int i = 0; i < COUNT; i++) {
                                RealmResults<Realm_PMS50003> result = realm.where(Realm_PMS50003.class)
                                        .equalTo("recordedTime", ((PMS50003) pms.get(i)).recordedTime)
                                        .findAll();
                                if (result.size() > 0) {
                                    result.remove(0);
                                } else {
                                    Log.w(TAG, "no such pm with recordedTime=" + ((PMS50003) pms.get(i)).recordedTime);
                                }
                            }
                            realm.commitTransaction();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.d(TAG, "upload batch failed. s:" + s);
                        }
                    });
                } else {
                    mSleepTime = Math.min(Math.max(SLEEP_MIN_TIME, 2 * mSleepTime), SLEEP_MAX_TIME);
                }
                try {
                    Log.d(TAG, "sleep: " + mSleepTime);
                    sleep(mSleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    class BtThread extends  ConnectThread {

        private final PmCollector mCollector;
        private InputStream mIn;
        private boolean mShouldQuit;

        public BtThread(String mac, PmCollector.PmCallback callback) {
            super(mac);

            mCollector = PmCollector.getInstance();
            mCollector.setCallback(callback);
        }

        @Override
        public void run() {
            mStatus = "thread try get socket.";
            super.run();
            mStatus = "thread end.";

            if (!mShouldQuit){

                mStatus = "restart service.";
                mUiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "restart service.");
                        startService(App.sInstance.getPref().getString(App.KEY_MAC, ""));
                    }
                }, 5 * 1000);
            }
        }

        public void quit() {
            mShouldQuit = true;

            mStatus = "ask thread quit.";
        }

        @Override
        protected void manageConnectedSocket(BluetoothSocket mmSocket) {
            super.manageConnectedSocket(mmSocket);

            mStatus = "thread open socket.";
            try {
                mIn = mmSocket.getInputStream();

                mStatus = "thread socket opened.";

                int count = -1;
                byte[] buffer = new byte[1024];
                while (!mShouldQuit && (count = mIn.read(buffer)) != -1) {
                    // FIXME do NOT work
//                    mCollector.onDataRcvd(ByteBuffer.wrap(buffer, 0, count).array());
                    for (int i = 0 ; i < count ; i++){
                        mCollector.onDataRcvd(buffer[i]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

//            stopForeground(true);
        }

        public String getStatistics() {
            return mTotalAck + "/" + mTotal;
        }
    }


    //http://developer.android.com/intl/zh-tw/guide/topics/connectivity/bluetooth.html#ConnectingDevices
    public static class ConnectThread extends Thread {
        //http://stackoverflow.com/questions/23963815/sending-data-from-android-to-arduino-with-hc-06-bluetooth-module
        // Well known SPP UUID
        private static final UUID MY_UUID =
                UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mDevice;
        private BluetoothAdapter mBluetoothAdapter;

        public ConnectThread(String mac) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mDevice = mBluetoothAdapter.getRemoteDevice(mac.toUpperCase());

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = mDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                Log.d(TAG, "EXP", connectException);
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.d(TAG, "EXP", closeException);
                }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }

        protected void manageConnectedSocket(BluetoothSocket mmSocket) {
            Log.d(TAG, "connect success.");
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}
