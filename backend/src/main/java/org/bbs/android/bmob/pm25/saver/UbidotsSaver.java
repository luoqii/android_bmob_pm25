package org.bbs.android.bmob.pm25.saver;

import android.os.AsyncTask;
import android.util.Log;

import com.ubidots.ApiClient;
import com.ubidots.Variable;

import org.bbs.android.bmob.pm25.backend.IPmThrottler;
import org.bbs.android.bmob.pm25.backend.PmCollector;
import org.bbs.android.pm25.library.PMS50003;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by bysong on 16-4-12.
 * http://ubidots.com/
 * luoqii
 */
public class UbidotsSaver implements PmCollector.PmCallback {
    private static final String TAG = UbidotsSaver.class.getSimpleName();

    private final String API_KEY = "7dc2fd83dae0242b063abed482b2e57edc5bdc87";
    private final String VARIABLE_ID = "57189ea57625420ae1393a28";

    private final IPmThrottler.TimeIntervalThrottler mThrottler;
    private ApiClient mApiClient;

    public UbidotsSaver(){

        mApiClient = new ApiClient(API_KEY);
        mThrottler = new IPmThrottler.TimeIntervalThrottler(10){
            @Override
            public void onReady(PMS50003 pm) {
                super.onReady(pm);
                save(pm);
            }
        };
    }

    @Override
    public void onPmAvailable(PMS50003 pm) {
        mThrottler.newPm(pm);
    }

    private void save(PMS50003 pm) {
        Variable pm25 = mApiClient.getVariable(VARIABLE_ID);

        pm25.saveValue(pm.pm2_5);
    }

    String toDataStr(PMS50003 pm){
        String str = "";
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        str = "{" +
//                "\"timestamp\":\"" + f.format(new Date(pm.recordedTime))  + "\"," +
                "\"value\":\"" + pm.pm2_5 + "\"" +
                "}"
        ;

        Log.d(TAG, "json:" + str);
        return str;
    }

    public class ApiUbidots extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            return null;
        }
    }
}
