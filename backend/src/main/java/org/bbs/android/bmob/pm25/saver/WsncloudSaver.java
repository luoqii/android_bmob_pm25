package org.bbs.android.bmob.pm25.saver;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.bbs.android.bmob.pm25.backend.IPmThrottler;
import org.bbs.android.bmob.pm25.backend.PmCollector;
import org.bbs.android.pm25.library.PMS50003;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by bysong on 16-4-14.
 *
 * http://www.wsncloud.com
 */
public class WsncloudSaver implements PmCollector.PmCallback {
    private static final String TAG = WsncloudSaver.class.getSimpleName();

    private static final String DOMAN = "http://api.wsncloud.com";
    private static final String PATH = "/data/v1/numerical/insert";
    private static final String APP_KEY = "2155b71b39a381dde8ee5f8ac281d79a";
    private static final String SENSOR_ID = "5710511de4b00415c43dd64e";
    private IPmThrottler.TimeIntervalThrottler mThrottler;

    public WsncloudSaver() {
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
        OkHttpClient c = new OkHttpClient();
//        RequestBody body = RequestBody.create(parse("application/json; charset=utf-8"), toDataStr(pm).getBytes());
        String param = "?ak="  + APP_KEY + "&id=" + SENSOR_ID + "&value=" + pm.pm2_5;
        Request r = new Request.Builder()
                .url(DOMAN + PATH + param)
//                .post(body)
                .build();

        c.newCall(r).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "onFailure. request:" + request + " e:" + e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.i(TAG, "onResponse. response:" + response);
            }
        });
    }

    String toDataStr(PMS50003 pm){
        String str = "";
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        str = "{" +
                "\"ak\":\"" + APP_KEY + "\"," +
                "\"id\":\"" + SENSOR_ID  + "\"," +
                "\"value\":\"" + pm.pm2_5 + "\"" +
                "}"
        ;

        Log.d(TAG, "json:" + str);
        return str;
    }
}
