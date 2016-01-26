package org.bbs.android.bmob.pm25;

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
import android.widget.Toast;

import java.util.Random;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;


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

        Bmob.DEBUG = BuildConfig.DEBUG;
        Bmob.initialize(this, "4d4e1d0aceaebfd7013592cc4b334c84");
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
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            view.findViewById(R.id.save).setOnClickListener(this);
            super.onViewCreated(view, savedInstanceState);
        }

        @Override
        public void onClick(View v) {
            PMS50003 pm = new PMS50003();
            Random r = new Random();
            pm.pm1_0_CF1 = r.nextInt(100);
            pm.pm2_5_CF1 = r.nextInt(100);
            pm.pm10_CF1 = r.nextInt(100);

            pm.pm1_0 = r.nextInt(100);
            pm.pm2_5 = r.nextInt(100);
            pm.pm10 = r.nextInt(100);

            pm.value_0_3 = r.nextInt(100);
            pm.value_0_5 = r.nextInt(100);
            pm.value_1 = r.nextInt(100);
            pm.value_2_5 = r.nextInt(100);
            pm.value_5 = r.nextInt(100);
            pm.value_10 = r.nextInt(100);
            pm.recordedTime = System.currentTimeMillis();

            pm.save(getActivity(), new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getActivity(), "save success.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(getActivity(), "save failed. " + s, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
