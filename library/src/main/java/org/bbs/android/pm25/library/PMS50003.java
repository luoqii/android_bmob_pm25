package org.bbs.android.pm25.library;

import java.util.Objects;

import cn.bmob.v3.BmobObject;

/**
 * Created by bysong on 16-1-26.
 *
 * PLANTOWER G5
 */
public class PMS50003 extends BmobObject {
    /** pm 1.0 CF = 1 ug/m^3*/
    public int pm1_0_CF1;
    /** pm 2.5 CF = 1 ug/m^3*/
    public int pm2_5_CF1;
    /** pm 10 CF = 1 ug/m^3*/
    public int pm10_CF1;

    /** pm 1.0 china ug/m^3*/
    public int pm1_0;
    /** pm 2.5 china ug/m^3*/
    public int pm2_5;
    /** pm 10  china ug/m^3*/
    public int pm10;

    /** .3um in 0.1l */
    public int value_0_3;
    /** .5um in 0.1l */
    public int value_0_5;
    /** 1um in 0.1l */
    public int value_1;
    /** 2.5um in 0.1l */
    public int value_2_5;
    /** 5um in 0.1l */
    public int value_5;
    /** 10um in 0.1l */
    public int value_10;

    public long recordedTime;

    public int reserve1;
    public int reserve2;
    public int reserve3;
    public int reserve4;
    public int reserve5;
    public int reserve6;

    public static PMS50003 fromRealm(Realm_PMS50003 realm_pm){
        PMS50003 pm = new PMS50003();

        pm.pm1_0 = realm_pm.getPm1_0();
        pm.pm2_5 = realm_pm.getPm2_5();
        pm.pm10 = realm_pm.getPm10();

        pm.pm1_0_CF1 = realm_pm.getPm1_0_CF1();
        pm.pm2_5_CF1 = realm_pm.getPm2_5_CF1();
        pm.pm10_CF1  = realm_pm.getPm10_CF1();

        pm.value_0_3 = realm_pm.getValue_0_3();
        pm.value_0_5 = realm_pm.getValue_0_5();
        pm.value_1   = realm_pm.getValue_1();
        pm.value_2_5 = realm_pm.getValue_2_5();
        pm.value_5   = realm_pm.getValue_5();
        pm.value_10  = realm_pm.getValue_10();

        pm.recordedTime = realm_pm.getRecordedTime();

        return pm;
    };

    //@Override
    public boolean sameAs(Realm_PMS50003 other) {
        if (other == null) {
            return false;
        }

        return (this.pm1_0 == other.getPm1_0())
                && (this.pm2_5 == other.getPm2_5())
                && (this.pm10 == other.getPm10())

                && (this.pm1_0_CF1 == other.getPm1_0_CF1())
                && (this.pm2_5_CF1 == other.getPm2_5_CF1())
                && (this.pm10_CF1 == other.getPm10_CF1())

                && (this.value_0_3 == other.getValue_0_3())
                && (this.value_0_5 == other.getValue_0_5())
                && (this.value_1 == other.getValue_1())
                && (this.value_2_5 == other.getValue_2_5())
                && (this.value_10 == other.getValue_10())

                && (this.recordedTime == other.getRecordedTime())
                ;
//        return super.equals(o);
    }

    @Override
    public String toString() {
        return "cf[" + pm1_0_CF1 + "," + pm2_5_CF1 + "," + pm10_CF1 + "],"
                + "[" + pm1_0 + "," + pm2_5 + "," + pm10 + "],"
                + "[" + value_0_3 + "," + value_0_5 + "," + value_1 + ","
                + value_2_5 + "," + value_5 + "," + value_10 + "],"
                + "[" + recordedTime + "]"
                ;
    }
}
