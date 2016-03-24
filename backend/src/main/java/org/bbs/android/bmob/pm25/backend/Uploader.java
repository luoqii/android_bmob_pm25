package org.bbs.android.bmob.pm25.backend;

import android.app.Application;
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
public class Uploader{

    private static final String TAG = Uploader.class.getSimpleName();
    private static final int ONGOING_NOTIFICATION_ID = R.layout.activity_main;
    private static Uploader sInstance;

    private final IBinder mLocalBinder = new LocalBinder();
    private final Application mApp;
    private BtThread mThread;

    private String mStatus = "init ...";

    public static Uploader getInstance(Application app){
        if (null == sInstance){
            sInstance = new Uploader(app);
        }

        return sInstance;
    }

    public Uploader(Application app){
        Log.d(TAG, "UploadService." + this);
        mApp = app;
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
                .putString(App.PREF_MAC, mac)
                .commit();

        if (null != mThread) {
            mThread.quit();
            mThread = null;
        }
        if (null == mThread) {
            mThread = new BtThread(mac);
            mThread.start();

            mStatus = "start thread.";
        }
    }

    public void stopSerivce() {
        Log.d(TAG, "stopService.");

        if (null != mThread) {
            mThread.quit();
            mThread = null;

//            stopForeground(true);
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

    class BtThread extends  ConnectThread {

        private final PmCollector mCollector;
        private InputStream mIn;
        private boolean mShouldQuit;

        private AtomicInteger mTotalAckPm;
        private AtomicInteger mTotalPm;
        private int mTotal;
        private int mTotalAck;
        private Handler mUiHandler;

        public BtThread(String mac) {
            super(mac);

            mTotalAckPm = new AtomicInteger();
            mTotalPm = new AtomicInteger();
            mUiHandler = new Handler();
            mCollector = PmCollector.getInstance();
            mCollector.setCallback(new PmCollector.PmCallback() {
                @Override
                public void onPmAvailable(PMS50003 pm) {
//                    mTotalPm.set(mTotalPm.get());
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTotalPm.set(mTotalPm.get() + 1);
                            mTotal++;
                            Log.d(TAG, "upload:" + mTotal + " success:" + mTotalAck);
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
                                }
                            });
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.w(TAG, "onFailure. i:" + i + " s:" + s);
                        }
                    });
                }
            });
        }

        @Override
        public void run() {
            mStatus = "thread try get socket.";
            super.run();
            mStatus = "thread end.";
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
