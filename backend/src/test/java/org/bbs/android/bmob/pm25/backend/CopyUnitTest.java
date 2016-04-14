package org.bbs.android.bmob.pm25.backend;

import org.bbs.android.pm25.library.AV_PMS50003;
import org.bbs.android.pm25.library.PMS50003;
import org.bbs.android.pm25.library.Realm_PMS50003;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CopyUnitTest {

    private int mCount;

    @Before
    public void setup(){
        mCount = 1000;
    }

    @Test
    public void test_realm_pm_from_pm() {
        for (int i = 0; i < mCount; i++) {
            realm_pm_from_pm();
        }
    }

    public void realm_pm_from_pm() {
        PMS50003 pm = TestActivity.randomPm();
        Realm_PMS50003 inter = Realm_PMS50003.fromPm(pm);
        assertTrue("\nExpected:" + pm + "\ninter   :" + inter, pm.sameAs(inter));
    }

    @Test
    public void test_pm_from_realm_pm() {
        for (int i = 0; i < mCount; i++) {
            pm_from_realm_pm();
        }
    }

    public void pm_from_realm_pm() {
        Realm_PMS50003 pm = Realm_PMS50003.fromPm(TestActivity.randomPm());
        PMS50003 inter = PMS50003.fromRealm(pm);
        assertTrue("\nExpected:" + pm + "\ninter   :" + inter, pm.sameAs(inter));
    }

    @Test
    public void test_av_pm_from_pm() {
        for (int i = 0; i < mCount; i++) {
            realm_pm_from_pm();
        }
    }

    public void av_pm_from_pm() {
        PMS50003 pm = TestActivity.randomPm();
        AV_PMS50003 inter = AV_PMS50003.fromPm(pm);
        assertTrue("\nExpected:" + pm + "\ninter   :" + inter, pm.sameAs(inter));
    }

    @Test
    public void test_pm_from_av_pm() {
        for (int i = 0; i < mCount; i++) {
            pm_from_av_pm();
        }
    }

    public void pm_from_av_pm() {
        AV_PMS50003 pm = AV_PMS50003.fromPm(TestActivity.randomPm());
        PMS50003 inter = PMS50003.fromAv(pm);
        assertTrue("\nExpected:" + pm + "\ninter   :" + inter, pm.sameAs(inter));
    }
}