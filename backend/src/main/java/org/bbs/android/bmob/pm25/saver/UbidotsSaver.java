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

//    private static final String DOMAN = "http://things.ubidots.com";
//    private static final String PATH = "/api/v1.6/variables/5711e45f76254237ac917c5b/values/";
//    private static final String APK_KEY = App.APK_KEY;
//    private static final String HEADER_U_APK_KEY = "U-ApiKey";
//    private static final String TOKEN = "QWhAbRs5G1PefcjEzP5A1kVNI53sls";

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
//        OkHttpClient c = new OkHttpClient();
//        RequestBody body = RequestBody.create(parse("application/json; charset=utf-8"), toDataStr(pm).getBytes());
//        String param = "?token=" + TOKEN;
//        Request r = new Request.Builder()
//                .url(DOMAN + PATH + param)
////                .header(HEADER_U_APK_KEY, APK_KEY)
//                .post(body)
//                .build();
//
//        c.newCall(r).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Log.e(TAG, "onFailure. request:" + request + " e:" + e);
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                Log.i(TAG, "onResponse. response:" + response);
//            }
//        });
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
