package org.bbs.android.bmob.pm25.backend;

import org.bbs.android.pm25.library.PMS50003;

/**
 * Created by bysong on 16-4-13.
 */
public interface IPmThrottler {

    public void newPm(PMS50003 pm);

    public void onReady(PMS50003 pm);

    public static class TimeIntervalThrottler implements  IPmThrottler {

        private boolean mFirst;
        private long mLastSec;
        private int mInterval;

        public TimeIntervalThrottler(int intervalInSec){
            mInterval = intervalInSec;
            mLastSec = System.currentTimeMillis() / 1000;
            mFirst = true;
        }

        @Override
        public void newPm(PMS50003 pm) {
            long sec = System.currentTimeMillis() / 1000;
            if (sec - mLastSec > mInterval || mFirst){
                onReady(pm);

                mLastSec = sec;
                mFirst = false;
            }
        }

        @Override
        public void onReady(PMS50003 pm) {

        }
    }
}
