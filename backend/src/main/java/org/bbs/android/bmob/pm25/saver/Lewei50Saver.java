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
 * luoqii
 * http://www.lewei50.com
 */
public class Lewei50Saver extends ThrottlerPmCollector {
    private static final String TAG = Lewei50Saver.class.getSimpleName();

    public Lewei50Saver() {
        super(10);
    }

    protected void save(PMS50003 pm) {
        OkHttpClient c = new OkHttpClient();
        RequestBody body = RequestBody.create(parse("application/json; charset=utf-8"), toDataStr(pm).getBytes());
        Request r = new Request.Builder()
                .url(App.LEWEI50_DOMAN + App.LEWEI50_PATH)
                .header(App.LEWEI50_HEADER_U_APP_KEY, App.LEWEI50_APK_KEY)
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
                "\"Name\":\"" + "pm2_5"  + "\"," +
                "\"Value\":\"" + pm.pm2_5 + "\"" +
                "}]"
        ;

        Log.d(TAG, "json:" + str);
        return str;
    }
}
