package org.bbs.android.pm25.library;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import io.realm.RealmObject;

/**
 * Created by bysong on 16-1-26.
 *
 */
@AVClassName("AV_PMS50003")
public class AV_PMS50003 extends AVObject {
    private int pm1_0_CF1;
    private int pm2_5_CF1;
    private int pm10_CF1;

    private int pm1_0;
    private int pm2_5;
    private int pm10;

    private int value_0_3;
    private int value_0_5;
    private int value_1;
    private int value_2_5;
    private int value_5;
    private int value_10;

    private long recordedTime;

    private int reserve1;
    private int reserve2;
    private int reserve3;
    private int reserve4;
    private int reserve5;
    private int reserve6;

    private boolean hasUploaded;

    public AV_PMS50003(){}

    public static AV_PMS50003 fromPm(PMS50003 pm){
        AV_PMS50003 p = new AV_PMS50003();

        p.setPm1_0_CF1(pm.pm1_0_CF1);
        p.setPm2_5_CF1(pm.pm2_5_CF1);
        p.setPm10_CF1(pm.pm10_CF1);

        p.setPm1_0(pm.pm1_0);
        p.setPm2_5(pm.pm2_5);
        p.setPm10(pm.pm10);

        p.setValue_0_3(pm.value_0_3);
        p.setValue_0_5(pm.value_0_5);
        p.setValue_1(pm.value_1);
        p.setValue_2_5(pm.value_2_5);
        p.setValue_5(pm.value_5);
        p.setValue_10(pm.value_10);

        p.setRecordedTime(pm.recordedTime);

        return p;
    }

    //@Override
    public boolean sameAs(PMS50003 other) {
        if (other == null) {
            return false;
        }

        return (this.pm1_0 == other.pm1_0)
                && (this.pm2_5 == other.pm2_5)
                && (this.pm10 == other.pm10)

                && (this.pm1_0_CF1 == other.pm1_0_CF1)
                && (this.pm2_5_CF1 == other.pm2_5_CF1)
                && (this.pm10_CF1 == other.pm10_CF1)

                && (this.value_0_3 == other.value_0_3)
                && (this.value_0_5 == other.value_0_5)
                && (this.value_1 == other.value_1)
                && (this.value_2_5 == other.value_2_5)
                && (this.value_10 == other.value_10)

                && (this.recordedTime == other.recordedTime)
                ;
//        return super.equals(o);
    }

    @Override
    public String toString() {
        return "cf[" + getPm1_0_CF1() + "," + getPm2_5_CF1() + "," + getPm10_CF1() + "],"
                + "[" + getPm1_0() + "," + getPm2_5() + "," + getPm10() + "],"
                + "[" + getValue_0_3() + "," + getValue_0_5() + "," + getValue_1() + ","
                + getValue_2_5() + "," + getValue_5() + "," + getValue_10() + "],"
                + "[" + recordedTime + "]"
                ;
    }

    /** pm 1.0 CF = 1 ug/m^3*/
    public int getPm1_0_CF1() {
        return getInt("pm1_0_CF1");
    }

    public void setPm1_0_CF1(int pm1_0_CF1) {
        put("pm1_0_CF1", pm1_0_CF1);
    }

    /** pm 2.5 CF = 1 ug/m^3*/
    public int getPm2_5_CF1() {
        return getInt("pm2_5_CF1");
    }

    public void setPm2_5_CF1(int pm2_5_CF1) {
        put("pm2_5_CF1", pm2_5_CF1);
    }

    /** pm 10 CF = 1 ug/m^3*/
    public int getPm10_CF1() {
        return getInt("pm10_CF1");
    }

    public void setPm10_CF1(int pm10_CF1) {
        put("pm10_CF1", pm10_CF1);
    }

    /** pm 1.0 china ug/m^3*/
    public int getPm1_0() {
        return getInt("pm1_0");
    }

    public void setPm1_0(int pm1_0) {
        put("pm1_0", pm1_0);
    }

    /** pm 2.5 china ug/m^3*/
    public int getPm2_5() {
        return getInt("pm2_5");
    }

    public void setPm2_5(int pm2_5) {
        put("pm2_5", pm2_5);
    }

    /** pm 10  china ug/m^3*/
    public int getPm10() {
        return getInt("pm10");
    }

    public void setPm10(int pm10) {
        put("pm10", pm10);
    }

    /** .3um in 0.1l */
    public int getValue_0_3() {
        return getInt("value_0_3");
    }

    public void setValue_0_3(int value_0_3) {
        put("value_0_3", value_0_3);
    }

    /** .5um in 0.1l */
    public int getValue_0_5() {
        return getInt("value_0_5");
    }

    public void setValue_0_5(int value_0_5) {
        put("value_0_5", value_0_5);
    }

    /** 1um in 0.1l */
    public int getValue_1() {
        return getInt("value_1");
    }

    public void setValue_1(int value_1) {
        put("value_1", value_1);
    }

    /** 2.5um in 0.1l */
    public int getValue_2_5() {
        return getInt("value_2_5");
    }

    public void setValue_2_5(int value_2_5) {
        put("value_2_5", value_2_5);
    }

    /** 5um in 0.1l */
    public int getValue_5() {
        return getInt("value_5");
    }

    public void setValue_5(int value_5) {
        put("value_5", value_5);
    }

    /** 10um in 0.1l */
    public int getValue_10() {
        return getInt("value_10");
    }

    public void setValue_10(int value_10) {
        put("value_10", value_10);
    }

    public long getRecordedTime() {
        return getLong("recordedTime");
    }

    public void setRecordedTime(long recordedTime) {
        put("recordedTime", recordedTime);
    }

    public int getReserve1() {
        return getInt("reserve1");
    }

    public void setReserve1(int reserve1) {
        put("reserve1", reserve1);
    }

    public int getReserve2() {
        return getInt("reserve2");
    }

    public void setReserve2(int reserve2) {
        put("reserve2", reserve2);
    }

    public int getReserve3() {
        return getInt("reserve3");
    }

    public void setReserve3(int reserve3) {
        put("reserve3", reserve3);
    }

    public int getReserve4() {
        return getInt("reserve4");
    }

    public void setReserve4(int reserve4) {
        put("reserve4", reserve4);
    }

    public int getReserve5() {
        return getInt("reserve5");
    }

    public void setReserve5(int reserve5) {
        put("reserve5", reserve5);
    }

    public int getReserve6() {
        return getInt("reserve6");
    }

    public void setReserve6(int reserve6) {
        put("reserve6", reserve6);
    }

    public boolean isHasUploaded() {
        return hasUploaded;
    }

    public void setHasUploaded(boolean hasUploaded) {
        this.hasUploaded = hasUploaded;
    }
}
