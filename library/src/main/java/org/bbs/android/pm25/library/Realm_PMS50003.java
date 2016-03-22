package org.bbs.android.pm25.library;

import io.realm.RealmObject;

/**
 * Created by bysong on 16-1-26.
 *
 * PLANTOWER G5
 */
public class Realm_PMS50003 extends RealmObject {
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

    public static Realm_PMS50003 fromPm(PMS50003 pm){
        Realm_PMS50003 p = new Realm_PMS50003();
        p.setPm1_0(pm.pm1_0);
        p.setPm2_5(pm.pm2_5);
        p.setPm10(pm.pm10);

        p.setPm1_0_CF1(pm.pm1_0_CF1);
        p.setPm2_5_CF1(pm.pm2_5_CF1);
        p.setPm1_0_CF1(pm.pm10_CF1);

        p.setValue_0_3(pm.value_0_3);
        p.setValue_0_5(pm.value_0_5);
        p.setValue_1(pm.value_1);
        p.setValue_2_5(pm.value_2_5);
        p.setValue_5(pm.value_5);
        p.setValue_10(pm.value_10);
        return p;
    }

    @Override
    public String toString() {
        return "cf[" + getPm1_0_CF1() + "," + getPm2_5_CF1() + "," + getPm10_CF1() + "]"
                + "[" + getPm1_0() + "," + getPm2_5() + "," + getPm10() + "]"
                + "[" + getValue_0_3() + "," + getValue_0_5() + "," + getValue_1() + ","
                + getValue_2_5() + "," + getValue_5() + "," + getValue_10() + "]"
                ;
    }

    /** pm 1.0 CF = 1 ug/m^3*/
    public int getPm1_0_CF1() {
        return pm1_0_CF1;
    }

    public void setPm1_0_CF1(int pm1_0_CF1) {
        this.pm1_0_CF1 = pm1_0_CF1;
    }

    /** pm 2.5 CF = 1 ug/m^3*/
    public int getPm2_5_CF1() {
        return pm2_5_CF1;
    }

    public void setPm2_5_CF1(int pm2_5_CF1) {
        this.pm2_5_CF1 = pm2_5_CF1;
    }

    /** pm 10 CF = 1 ug/m^3*/
    public int getPm10_CF1() {
        return pm10_CF1;
    }

    public void setPm10_CF1(int pm10_CF1) {
        this.pm10_CF1 = pm10_CF1;
    }

    /** pm 1.0 china ug/m^3*/
    public int getPm1_0() {
        return pm1_0;
    }

    public void setPm1_0(int pm1_0) {
        this.pm1_0 = pm1_0;
    }

    /** pm 2.5 china ug/m^3*/
    public int getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(int pm2_5) {
        this.pm2_5 = pm2_5;
    }

    /** pm 10  china ug/m^3*/
    public int getPm10() {
        return pm10;
    }

    public void setPm10(int pm10) {
        this.pm10 = pm10;
    }

    /** .3um in 0.1l */
    public int getValue_0_3() {
        return value_0_3;
    }

    public void setValue_0_3(int value_0_3) {
        this.value_0_3 = value_0_3;
    }

    /** .5um in 0.1l */
    public int getValue_0_5() {
        return value_0_5;
    }

    public void setValue_0_5(int value_0_5) {
        this.value_0_5 = value_0_5;
    }

    /** 1um in 0.1l */
    public int getValue_1() {
        return value_1;
    }

    public void setValue_1(int value_1) {
        this.value_1 = value_1;
    }

    /** 2.5um in 0.1l */
    public int getValue_2_5() {
        return value_2_5;
    }

    public void setValue_2_5(int value_2_5) {
        this.value_2_5 = value_2_5;
    }

    /** 5um in 0.1l */
    public int getValue_5() {
        return value_5;
    }

    public void setValue_5(int value_5) {
        this.value_5 = value_5;
    }

    /** 10um in 0.1l */
    public int getValue_10() {
        return value_10;
    }

    public void setValue_10(int value_10) {
        this.value_10 = value_10;
    }

    public long getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(long recordedTime) {
        this.recordedTime = recordedTime;
    }

    public int getReserve1() {
        return reserve1;
    }

    public void setReserve1(int reserve1) {
        this.reserve1 = reserve1;
    }

    public int getReserve2() {
        return reserve2;
    }

    public void setReserve2(int reserve2) {
        this.reserve2 = reserve2;
    }

    public int getReserve3() {
        return reserve3;
    }

    public void setReserve3(int reserve3) {
        this.reserve3 = reserve3;
    }

    public int getReserve4() {
        return reserve4;
    }

    public void setReserve4(int reserve4) {
        this.reserve4 = reserve4;
    }

    public int getReserve5() {
        return reserve5;
    }

    public void setReserve5(int reserve5) {
        this.reserve5 = reserve5;
    }

    public int getReserve6() {
        return reserve6;
    }

    public void setReserve6(int reserve6) {
        this.reserve6 = reserve6;
    }

    public boolean isHasUploaded() {
        return hasUploaded;
    }

    public void setHasUploaded(boolean hasUploaded) {
        this.hasUploaded = hasUploaded;
    }
}
