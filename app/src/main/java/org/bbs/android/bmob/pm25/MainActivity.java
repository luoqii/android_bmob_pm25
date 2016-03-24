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

import org.bbs.android.pm25.library.PMS50003;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class MainActivity extends AppCompatActivity {

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

        return super.onOptionsItemSelected(item);
    }

    public static class LineCharFragment extends Fragment
//            implements View.OnClickListener
    {

        public static final String PM_2_5 = "pm 2.5";
        public static final String PM_1_0 = "pm 1.0";
        public static final String PM_0_3 = "pm 0.3";
        public static final String VALUE_0_3 = "value 0.3";
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
            query.setLimit(500);

            query.findObjects(getActivity(), new FindListener<PMS50003>() {
                @Override
                public void onSuccess(List<PMS50003> datas) {
                    mData.addAll(datas);
                    final int L = mData.size();
                    toast("查询成功：共" + datas.size() + "条数据。");

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
                    for (int i = 0; i < L; i++) {
                        xVals.add(i + "");
                    }
                    LineData data = new LineData(xVals, dataSets);
                    mChart.setData(data);
                    mChart.invalidate();
                }

                @Override
                public void onError(int code, String msg) {
                    // TODO Auto-generated method stub
                    toast("查询失败：" + msg);
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
        @Bind(R.id.pm_value_0_3) TextView mPm03ValueV;
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
            ButterKnife.bind(this,v);
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
                        mPm25V.setText(pm.pm2_5 + "");
                        CharSequence text =
                                DateUtils.getRelativeTimeSpanString(pm.recordedTime,
                                            System.currentTimeMillis(),
                                            0,
                                            DateUtils.FORMAT_NUMERIC_DATE);
                        mUpdateTimeV.setText(text);

                        mPm03ValueV.setText(pm.value_0_3 + "");
                    }
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
    }
}
