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

import org.bbs.android.bmob.pm25.saver.OnenetSaver;
import org.bbs.android.bmob.pm25.saver.WsncloudSaver;
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
        // bangbang.s http://bmob.cn/app/browser/83253
        Bmob.initialize(this, "630486c87a1c6070c7679ff7be884730");
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
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            mPmCollector.addCallback(new WsncloudSaver());
            mPmCollector.addCallback(new OnenetSaver());
//            mPmCollector.addCallback(new AVSaver());
//            mPmCollector.addCallback(new YeelinkSaver());
//            mPmCollector.addCallback(new Lewei50Saver());
//            mPmCollector.addCallback(new BmobSaver(activity.getApplication()));
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
            getView().postDelayed(this, 1 * 1000);
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
