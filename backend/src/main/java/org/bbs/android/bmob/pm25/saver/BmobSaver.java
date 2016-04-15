package org.bbs.android.bmob.pm25.saver;

import android.app.Application;
import android.util.Log;

import org.bbs.android.bmob.pm25.backend.App;
import org.bbs.android.bmob.pm25.backend.IPmThrottler;
import org.bbs.android.bmob.pm25.backend.PmCollector;
import org.bbs.android.pm25.library.AV_PMS50003;
import org.bbs.android.pm25.library.PMS50003;
import org.bbs.android.pm25.library.Realm_PMS50003;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by bysong on 16-4-12.
 */
public class BmobSaver extends Thread
        implements PmCollector.PmCallback {
    private static final String TAG = BmobSaver.class.getSimpleName();
    private final Application mApp;

    private boolean mShouldQuit;
    private static final int SLEEP_MIN_TIME = 5 * 1000;
    private static final int SLEEP_MAX_TIME = 1 * 60 * 60 * 1000;
    private int mSleepTime;

    private final IPmThrottler.TimeIntervalThrottler mThrottler;

    public BmobSaver(Application app) {
        mSleepTime = SLEEP_MIN_TIME;
        mApp = app;
        mThrottler = new IPmThrottler.TimeIntervalThrottler(10){
            @Override
            public void onReady(PMS50003 pm) {
                super.onReady(pm);
                save(pm);
            }
        };
        start();
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
            final RealmResults<Realm_PMS50003> result = realm.where(Realm_PMS50003.class)
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

                    // TODO batch this op
                    AV_PMS50003 avPm = AV_PMS50003.fromPm(PMS50003.fromRealm(result.get(i)));
                    avPm.saveInBackground();
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

    @Override
    public void onPmAvailable(final PMS50003 pm) {
        save(pm);
    }

    private void save(final PMS50003 pm) {
        pm.save(mApp, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.w(TAG, "onFailure. i:" + i + " s:" + s);

                Realm realm = Realm.getInstance(App.sRealmConfig);
                realm.beginTransaction();
                realm.copyToRealm(Realm_PMS50003.fromPm(pm));

                realm.commitTransaction();
                Log.w(TAG, "save by ream. pm:" + pm);
            }
        });
    }

}
