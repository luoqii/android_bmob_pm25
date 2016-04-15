package org.bbs.android.bmob.pm25.saver;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.bbs.android.bmob.pm25.backend.IPmThrottler;
import org.bbs.android.bmob.pm25.backend.PmCollector;
import org.bbs.android.pm25.library.PMS50003;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.squareup.okhttp.MediaType.parse;

/**
 * Created by bysong on 16-4-14.
 *
 * http://open.iot.10086.cn
 */
public class OnenetSaver implements PmCollector.PmCallback {
    private static final String TAG = OnenetSaver.class.getSimpleName();

    private static final String DOMAN = "http://api.heclouds.com";
    private static final String PATH = "/devices/1086201/datapoints";
    private static final String APP_KEY = " DmeFcOmio4W38ocK5uT=TN02qi4=";
    private static final String HEADER_API_KEY = "api-key";
    private IPmThrottler.TimeIntervalThrottler mThrottler;

    public OnenetSaver() {
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
        RequestBody body = RequestBody.create(parse("application/json; charset=utf-8"), toDataStr(pm).getBytes());
        String param = "?type=3";
        Request r = new Request.Builder()
                .url(DOMAN + PATH + param)
                .post(body)
                .header(HEADER_API_KEY, APP_KEY)
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
                "\"pm25\":\"" + pm.pm2_5 + "\"" +
                "}"
        ;

        Log.d(TAG, "json:" + str);
        return str;
    }
}
