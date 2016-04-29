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

import static com.squareup.okhttp.MediaType.parse;

/**
 * Created by bysong on 16-4-14.
 *
 * http://open.iot.10086.cn
 * 189-xxxx-xxxx
 */
public class OnenetSaver extends ThrottlerPmCollector {
    private static final String TAG = OnenetSaver.class.getSimpleName();

    public OnenetSaver() {
        super(10);
    }

    protected void save(PMS50003 pm) {
        OkHttpClient c = new OkHttpClient();
        RequestBody body = RequestBody.create(parse("application/json; charset=utf-8"), toDataStr(pm).getBytes());
        String param = "?type=3";
        Request r = new Request.Builder()
                .url(App.ONENET_DOMAN + App.ONENET_PATH + param)
                .post(body)
                .header(App.ONENET_HEADER_API_KEY, App.ONENET_APP_KEY)
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
        str = "{" +
                "\"pm2_5\":\"" + pm.pm2_5 + "\"" +
                "}"
        ;

        Log.d(TAG, "json:" + str);
        return str;
    }
}
