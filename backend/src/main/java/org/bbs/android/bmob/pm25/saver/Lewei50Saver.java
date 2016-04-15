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
 */
public class Lewei50Saver implements PmCollector.PmCallback {
    private static final String TAG = Lewei50Saver.class.getSimpleName();

    private static final String DOMAN = "http://www.lewei50.com";
    private static final String PATH = "/api/V1//gateway/UpdateSensors/01";
    private static final String APK_KEY = "f4325d957ae2490d9376b65869dd90ee";
    private static final String HEADER_U_APP_KEY = "userkey";
    private IPmThrottler.TimeIntervalThrottler mThrottler;

    public Lewei50Saver() {
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
        Request r = new Request.Builder()
                .url(DOMAN + PATH)
                .header(HEADER_U_APP_KEY, APK_KEY)
                .post(body)
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
        str = "[{" +
                "\"Name\":\"" + "pm25"  + "\"," +
                "\"Value\":\"" + pm.pm2_5 + "\"" +
                "}]"
        ;

        Log.d(TAG, "json:" + str);
        return str;
    }
}
