package org.bbs.android.bmob.pm25.saver;

import org.bbs.android.bmob.pm25.backend.IPmThrottler;
import org.bbs.android.bmob.pm25.backend.PmCollector;
import org.bbs.android.pm25.library.PMS50003;

/**
 * Created by bysong on 16-4-27.
 */
public abstract class ThrottlerPmCollector  implements PmCollector.PmCallback {
    private final IPmThrottler.TimeIntervalThrottler mThrottler;

    public ThrottlerPmCollector(int time){
        mThrottler = new IPmThrottler.TimeIntervalThrottler(time){
            @Override
            public void onReady(PMS50003 pm) {
                super.onReady(pm);
                save(pm);
            }
        };
    }

    protected abstract void save(PMS50003 pm);

    @Override
    final public void onPmAvailable(PMS50003 pm) {
        mThrottler.newPm(pm);
    }
}
