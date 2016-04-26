package org.bbs.android.bmob.pm25;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.common.api.GoogleApiClient;

import org.bbs.android.commonlib.activity.LogcatActivity;
import org.bbs.android.pm25.library.AppBaseActivity;
import org.bbs.android.pm25.library.PMS50003;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class MainActivity extends AppBaseActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new LineCharFragment())
                    .add(R.id.container, new Pm25Fragment())
                    .commit();
        }
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
        }

        return super.onOptionsItemSelected(item);
    }

    public static class LineCharFragment extends Fragment
//            implements View.OnClickListener
    {

        public static final String PM_2_5 = "pm 2.5";
        public static final String PM_1_0 = "pm 1.0";
        public static final String PM_0_3 = "pm 0.3";
        public static final String VALUE_0_3 = "value 0.3";
        private static final String TAG = LineCharFragment.class.getSimpleName();
        private LineChart mChart;
        List<PMS50003> mData;

        public LineCharFragment() {
            setHasOptionsMenu(true);
            mData = new ArrayList<>();
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.menu_fragment_main, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case R.id.togglle_03:
                    toggle03();
                    break;
            }
            return super.onOptionsItemSelected(item);
        }

        private void toggle03() {
            LineData lineData = mChart.getData();
            final int COUNT = lineData.getDataSetCount();
            for (int i = 0 ; i < COUNT; i++){
                ILineDataSet dataSet = lineData.getDataSetByIndex(i);
                if (dataSet.getLabel().equalsIgnoreCase(VALUE_0_3)){
                    lineData.removeDataSet(dataSet);
                    lineData.notifyDataChanged();
                    mChart.invalidate();
                    return;
                }
            }

            final int L = mData.size();
            ArrayList<Entry> vals03 = new ArrayList<Entry>();
            for (int i = 0; i < L; i++) {
                Entry e = new Entry(mData.get(i).value_0_3, i);
                vals03.add(e);
            }
            LineDataSet set03 = new LineDataSet(vals03, VALUE_0_3);
            set03.setColor(Color.YELLOW);

//            lineData.getDataSets().add(set03);
            lineData.addDataSet(set03);

            // FIXME this will crach app, why???
//            lineData.notifyDataChanged();
            mChart.notifyDataSetChanged();

            mChart.invalidate();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            mChart = (LineChart) view.findViewById(R.id.chart);
            mChart.setDescription("pm 2.5");
            super.onViewCreated(view, savedInstanceState);

            initData();
        }

        void initData() {
            BmobQuery<PMS50003> query = new BmobQuery<PMS50003>();
            query.setLimit(1000);
            query.order("-recordedTime");

            query.findObjects(getActivity(), new FindListener<PMS50003>() {
                @Override
                public void onSuccess(List<PMS50003> datas) {
                    for (int i = datas.size() - 1 ; i > 0  ; i --) {
                        mData.add(datas.get(i));
                    }
                    final int L = mData.size();
                    toast("查询成功：共" + datas.size() + "条数据。");
                    Log.e(TAG, "查询成功：共" + datas.size() + "条数据。");

                    ArrayList<Entry> valsPm25 = new ArrayList<Entry>();
                    for (int i = 0; i < L; i++) {
                        Entry e = new Entry(mData.get(i).pm2_5, i);
                        valsPm25.add(e);
                    }
                    LineDataSet setPm25 = new LineDataSet(valsPm25, PM_2_5);
                    setPm25.setColor(Color.RED);

                    ArrayList<Entry> valsPm10 = new ArrayList<Entry>();
                    for (int i = 0; i < L; i++) {
                        Entry e = new Entry(mData.get(i).pm1_0, i);
                        valsPm10.add(e);
                    }
                    LineDataSet setPm10 = new LineDataSet(valsPm10,  PM_1_0);

                    // use the interface ILineDataSet
                    ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                    dataSets.add(setPm25);
                    dataSets.add(setPm10);

                    ArrayList<String> xVals = new ArrayList<String>();
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm:ss");
                    for (int i = 0; i < L; i++) {
                        xVals.add(df.format(new Date(mData.get(i).recordedTime)));
                    }
                    LineData data = new LineData(xVals, dataSets);
                    mChart.setData(data);
                    mChart.invalidate();

//                    BmobObject.
                }

                @Override
                public void onError(int code, String msg) {
                    // TODO Auto-generated method stub
                    toast("查询失败：" + msg);
                    Log.e(TAG, "查询失败：" + msg);
                }
            });
        }

//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.save_batch:
////                    saveBatch();
//                    break;
//                case R.id.save:
////                    save();
//                    break;
//            }
//        }

        void toast(String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public static class Pm25Fragment extends  Fragment {
        private static final int DELAY = 1 * 1000;

        @Bind(R.id.pm25) TextView mPm25V;
        @Bind(R.id.update_time) TextView mUpdateTimeV;
        private Handler mHandler;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    update();
                }
            };
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_pm25, null);
            ButterKnife.bind(this, v);
            return v;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }

        @Override
        public void onResume() {
            super.onResume();

            mHandler.sendEmptyMessage(0);
        }

        @Override
        public void onPause() {
            super.onPause();

            mHandler.removeMessages(0);
        }

        void update(){
            BmobQuery<PMS50003> query = new BmobQuery<PMS50003>();
            query.setLimit(1);
            query.order("-recordedTime");
            query.findObjects(getContext(), new FindListener<PMS50003>() {
                @Override
                public void onSuccess(List<PMS50003> list) {
                    PMS50003 pm = list.get(0);
                    if (null != pm) {
                        CharSequence text =
                                DateUtils.getRelativeTimeSpanString(pm.recordedTime,
                                        System.currentTimeMillis(),
                                        0,
                                        DateUtils.FORMAT_NUMERIC_DATE);
                        mUpdateTimeV.setText(text);
                        mPm25V.setText(pm.pm2_5 + "");

                        ArrayList<View> views = new ArrayList<View>();
                        getView().findViewsWithText(views, "vaules", View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                        initValue(views.get(0), " 0.3: ", pm.value_0_3 + "μg/m³");
                        initValue(views.get(1), " 0.5: ", pm.value_0_5 + "μg/m³");
                        initValue(views.get(2), " 1.0: ", pm.value_1 + "μg/m³");
                        initValue(views.get(3), " 2.5: ", pm.value_2_5 + "μg/m³");
                        initValue(views.get(4), " 5.0: ", pm.value_5 + "μg/m³");
                        initValue(views.get(5), "10.0: ", pm.value_10 + "μg/m³");

                        initValue(views.get(6), " pm1.0: ", pm.pm1_0);
                        initValue(views.get(7), " pm2.5: ", pm.pm2_5);
                        initValue(views.get(8), "  pm10: ", pm.pm10);

                        initValue(views.get(9), "  pm1.0(CF): ", pm.pm1_0_CF1);
                        initValue(views.get(10), " pm2.5(CF): ", pm.pm2_5_CF1);
                        initValue(views.get(11), "  pm10(CF): ", pm.pm10_CF1);
                    }
                }


                private void initValue(View view, String title, int value) {
                    initValue(view, title, value + "");
                }

                private void initValue(View view, String title, String value) {
                    ((TextView) ((ViewGroup) view).getChildAt(0)).setText(title);
                    ((TextView) ((ViewGroup) view).getChildAt(1)).setText(value);
                }

                @Override
                public void onError(int i, String s) {

                }
            });

            mHandler.sendEmptyMessageDelayed(0, DELAY);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            ButterKnife.unbind(this);
        }

        @OnClick(R.id.pm25)
        void showPm25ByLineChart() {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LineCharFragment())
                    .addToBackStack("chart")
                    .commit();
        }
    }
}
