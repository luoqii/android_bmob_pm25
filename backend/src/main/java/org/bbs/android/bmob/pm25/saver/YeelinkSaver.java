package org.bbs.android.bmob.pm25.saver;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.bbs.android.bmob.pm25.backend.App;
import org.bbs.android.bmob.pm25.backend.IPmThrottler;
import org.bbs.android.bmob.pm25.backend.PmCollector;
import org.bbs.android.pm25.library.PMS50003;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.squareup.okhttp.MediaType.parse;

/**
 * Created by bysong on 16-4-12.
 */
public class YeelinkSaver implements PmCollector.PmCallback {
    private static final String TAG = YeelinkSaver.class.getSimpleName();

    private static final String DOMAN = App.DOMAN;
    private static final String PATH = App.PATH;
    private static final String APK_KEY = App.APK_KEY;
    private static final String HEADER_U_APK_KEY = "U-ApiKey";
    private final IPmThrottler.TimeIntervalThrottler mThrottler;

    public YeelinkSaver(){
        //http://www.yeelink.net/develop/api#create_datapoint
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
                .header(HEADER_U_APK_KEY, APK_KEY)
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
        str = "{" +
                "\"timestamp\":\"" + f.format(new Date(pm.recordedTime))  + "\"," +
                "\"value\":\"" + pm.pm2_5 + "\"" +
                "}"
        ;

        Log.d(TAG, "json:" + str);
        return str;
    }
}
