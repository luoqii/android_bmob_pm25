package org.bbs.android.bmob.pm25.saver;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;

import org.bbs.android.bmob.pm25.backend.PmCollector;
import org.bbs.android.pm25.library.AV_PMS50003;
import org.bbs.android.pm25.library.PMS50003;

/**
 * Created by bysong on 16-4-12.
 */
public class AVSaver implements PmCollector.PmCallback {
    private static final String TAG = AVSaver.class.getSimpleName();

    @Override
    public void onPmAvailable(PMS50003 pm) {
        AV_PMS50003 avPm = AV_PMS50003.fromPm(pm);
        avPm.saveInBackground(new AV_SaveCallback());
    }

    private class AV_SaveCallback extends SaveCallback {
        @Override
        public void done(AVException e) {
            if (e == null) {
                Log.d(TAG, "AV_PMS50003 save success!");
            }
        }
    }
}
