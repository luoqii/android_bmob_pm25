package org.bbs.android.bmob.pm25.backend;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final int REQUEST_PICK_BT = 1;
        private static final long DELAY = 5 * 1000;
        private final Handler mUiHandler;
        @Bind(R.id.status)
        TextView mStatusV;

        @Bind(R.id.statistics)
        TextView mStatisticsV;

        @Bind(R.id.start)
        Button mStartB;
        @Bind(R.id.auto_start)
        CheckBox mAutoStart;

        @Bind(R.id.stop)
        Button mStop;

        @Bind({R.id.start, R.id.stop})
        List<Button> mButtons;

        static final ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
            @Override public void apply(View view, int index) {
                view.setEnabled(false);
            }
        };
        static final ButterKnife.Setter<View, Boolean> ENABLED = new ButterKnife.Setter<View, Boolean>() {
            @Override public void set(View view, Boolean value, int index) {
                view.setEnabled(value);
            }
        };

        private boolean mBound;
        private UploadService mService;
        private ServiceConnection mConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                UploadService.LocalBinder binder = (UploadService.LocalBinder) service;
                mService = binder.getService();
                mBound = true;

                ButterKnife.apply(mButtons, ENABLED, true);
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                mBound = false;

                ButterKnife.apply(mButtons, DISABLE);
            }
        };

        public PlaceholderFragment() {
            mUiHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    updateUi();

                    sendEmptyMessageDelayed(0, DELAY);
                }
            };
        }

        private void updateUi() {
            if (null != mService && getActivity() != null) {
                mStatisticsV.setText(mService.getStatistics());
                mStatusV.setText(mService.getStatus());
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ButterKnife.bind(this, rootView);

            mAutoStart.setChecked(App.sInstance.getPref().getBoolean(App.PREF_AUTO_START_AT_BOOT, false));
            mAutoStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    App.sInstance.getPref().edit()
                            .putBoolean(App.PREF_AUTO_START_AT_BOOT, isChecked)
                            .commit();
                }
            });
            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();
            Intent service = new Intent(getActivity(), UploadService.class);
            getActivity().bindService(service, mConnection, Context.BIND_AUTO_CREATE);
        }

        @Override
        public void onResume() {
            super.onResume();
            mUiHandler.removeMessages(0);
            mUiHandler.sendEmptyMessageDelayed(0, DELAY);
        }

        @Override
        public void onStop() {
            super.onStop();
            getActivity().unbindService(mConnection);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            ButterKnife.unbind(this);
        }

        @OnClick(R.id.start)
        void startService(){
            Intent pickBt = new Intent(getActivity(), BtDeviceActivity.class);
            startActivityForResult(pickBt, REQUEST_PICK_BT);
        }

        void doStartService(String mac) {
            mService.startService(mac);
            mUiHandler.sendEmptyMessage(0);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK && REQUEST_PICK_BT == requestCode){
                String mac = data.getStringExtra(BtDeviceActivity.EXTRA_MAC);

                doStartService(mac);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        @OnClick(R.id.stop)
        void stopService(){
            mService.stopSerivce();
        }
    }
}
