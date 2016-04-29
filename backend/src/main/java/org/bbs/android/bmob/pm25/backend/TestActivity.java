package org.bbs.android.bmob.pm25.backend;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVOSCloud;

import org.bbs.android.bmob.pm25.saver.AVSaver;
import org.bbs.android.bmob.pm25.saver.BmobSaver;
import org.bbs.android.bmob.pm25.saver.CarriotsSaver;
import org.bbs.android.bmob.pm25.saver.Lewei50Saver;
import org.bbs.android.bmob.pm25.saver.OnenetSaver;
import org.bbs.android.bmob.pm25.saver.UbidotsRestApiSaver;
import org.bbs.android.bmob.pm25.saver.UbidotsSaver;
import org.bbs.android.bmob.pm25.saver.WsncloudSaver;
import org.bbs.android.bmob.pm25.saver.YeelinkSaver;
import org.bbs.android.commonlib.activity.LogcatActivity;
import org.bbs.android.pm25.library.PMS50003;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;


public class TestActivity extends ActionBarActivity {

    private static final String TAG = TestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Bmob.DEBUG = true;
        // http://bmob.cn/app/secret/83253
        // bangbang.s
        Bmob.initialize(this, "630486c87a1c6070c7679ff7be884730");

        //https://leancloud.cn/data.html?appid=l9SCds6HwQ3cAY2nT0p4SkUU-gzGzoHsz#/
        AVOSCloud.initialize(this, "l9SCds6HwQ3cAY2nT0p4SkUU-gzGzoHsz", "XzFCHOE21vAJ4mX991cneb3u");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logcat){
            LogcatActivity.start(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static PMS50003 randomPm() {
        PMS50003 pm = new PMS50003();
        Random r = new Random();
        long time = System.currentTimeMillis();
        long millisPerHour = 1000 * 60 * 60;
        long t = time % millisPerHour;
        float x = t / (float) millisPerHour;
        double y = Math.sin(x * 2 * 3.1415926);
        int base = (int) (y * 100);

        int random_range = 4;
        pm.pm1_0_CF1 = (base + r.nextInt(random_range));
        pm.pm2_5_CF1 = (base + r.nextInt(random_range));
        pm.pm10_CF1 = (base + r.nextInt(random_range));

        pm.pm1_0 = (base + r.nextInt(random_range));
        pm.pm2_5 = (base + r.nextInt(random_range));
        pm.pm10 = (base + r.nextInt(random_range));

        pm.value_0_3 = (base + r.nextInt(random_range));
        pm.value_0_5 = (base + r.nextInt(random_range));
        pm.value_1 = (base + r.nextInt(random_range));
        pm.value_2_5 = (base + r.nextInt(random_range));
        pm.value_5 = (base + r.nextInt(random_range));
        pm.value_10 = (base + r.nextInt(random_range));

        pm.recordedTime = (System.currentTimeMillis());


        return pm;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener, Runnable {
        private final PmCollector mPmCollector;

        public PlaceholderFragment() {
            mPmCollector = PmCollector.getInstance();

            // FIXME must do this at Activity.onCreate(), why ????
            Bmob.DEBUG = true;
            // http://bmob.cn/app/secret/83253
            // bangbang.s
            Bmob.initialize(this.getActivity(), "630486c87a1c6070c7679ff7be884730");

            App.LEWEI50_PATH = "/api/V1/gateway/UpdateSensors/02";
            //http://open.iot.10086.cn/device/detail?pid=60394&device_id=1084194
            App.ONENET_PATH = "/devices/1084194/datapoints";
            App.YEELINK_PATH = "/v1.0/device/347108/sensor/387404/datapoints";
            App.WSN_CLOUD_SENSOR_ID = "5720619be4b00415c43e615b";
            App.UBIDOTS_PATH = "/api/v1.6/variables/57206b9576254236d51d8322/values/";
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            mPmCollector.addCallback(new UbidotsRestApiSaver());
//            mPmCollector.addCallback(new UbidotsSaver());
            mPmCollector.addCallback(new WsncloudSaver());
            mPmCollector.addCallback(new OnenetSaver());
            mPmCollector.addCallback(new AVSaver());
            mPmCollector.addCallback(new CarriotsSaver());
            mPmCollector.addCallback(new YeelinkSaver());

              // FIXME can not upload new data
//            mPmCollector.addCallback(new Lewei50Saver());

            mPmCollector.addCallback(new BmobSaver(activity.getApplication()));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_test, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            view.findViewById(R.id.save).setOnClickListener(this);
            view.findViewById(R.id.save_batch).setOnClickListener(this);
            view.findViewById(R.id.start_upload).setOnClickListener(this);
            super.onViewCreated(view, savedInstanceState);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.save_batch:
                    saveBatch();
                    break;
                case R.id.save:
                    save();
                    break;
                case R.id.start_upload:
                    scheduleStartUpload();
                    break;
            }
        }

        private void scheduleStartUpload() {
            if (null != getView()) {
                getView().postDelayed(this, 1 * 1000);
            }
        }

        private void doStartUpload() {
            save();

            scheduleStartUpload();
        }

        private void save() {
            PMS50003 pm = randomPm();
//            pm.save(getActivity(), new ToastSaveListener());
//
//            AV_PMS50003 rPm = AV_PMS50003.fromPm(pm);
//            rPm.saveInBackground(new AV_SaveCallback());

            mPmCollector.pmAvaiable(pm);
        }

        private void saveBatch() {
            List<BmobObject> pms = new ArrayList<BmobObject>();
            pms.add(randomPm());
            pms.add(randomPm());
            pms.add(randomPm());
            pms.add(randomPm());
            pms.add(randomPm());
            pms.add(randomPm());
            pms.add(randomPm());
            pms.add(randomPm());
            pms.add(randomPm());
            pms.add(randomPm());

            new BmobObject().insertBatch(getActivity(), pms, new ToastSaveListener());
        }

        @Override
        public void run() {
            doStartUpload();
        }

        private class ToastSaveListener extends SaveListener {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "save success.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getActivity(), "save failed. " + s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
