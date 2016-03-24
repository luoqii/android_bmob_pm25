package org.bbs.android.bmob.pm25.backend;

import org.bbs.android.pm25.library.PMS50003;
import org.bbs.android.pm25.library.Realm_PMS50003;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CopyUnitTest {
    @Test
    public void test_pm_realm_pm() {
        for (int i = 0; i < 10000; i++) {
            pm_realm();
        }
    }

    public void pm_realm() {
        PMS50003 pm = TestActivity.randomPm();
        Realm_PMS50003 inter = Realm_PMS50003.fromPm(pm);
        assertTrue("\nExpected:" + pm + "\ninter   :" + inter, pm.sameAs(inter));
    }

    @Test
    public void test_realm_pm_realm() {
        for (int i = 0; i < 10000; i++) {
            realm_pm_realm();
        }
    }

    public void realm_pm_realm() {
        Realm_PMS50003 pm = Realm_PMS50003.fromPm(TestActivity.randomPm());
        PMS50003 inter = PMS50003.fromRealm(pm);
        assertTrue("\nExpected:" + pm + "\ninter   :" + inter, pm.saveAs(inter));
    }
}