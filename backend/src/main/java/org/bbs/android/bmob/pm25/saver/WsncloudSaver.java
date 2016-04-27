package org.bbs.android.bmob.pm25.saver;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.bbs.android.bmob.pm25.backend.App;
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
 * bb.s@gmail.com
 */
public class WsncloudSaver extends ThrottlerPmCollector {
    private static final String TAG = WsncloudSaver.class.getSimpleName();

    private IPmThrottler.TimeIntervalThrottler mThrottler;

    public WsncloudSaver() {
        super(4);
    }

    protected void save(PMS50003 pm) {
        OkHttpClient c = new OkHttpClient();
        String param = "?ak="  + App.WSN_CLOUD_APP_KEY + "&id=" + App.WSN_CLOUD_SENSOR_ID + "&value=" + pm.pm2_5;
        Request r = new Request.Builder()
                .url(App.WSN_CLOUD_DOMAN + App.WSN_CLOUD_PATH + param)
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
}
