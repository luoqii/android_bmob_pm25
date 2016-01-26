package org.bbs.android.bmob.pm25;

import cn.bmob.v3.BmobObject;

/**
 * Created by bysong on 16-1-26.
 *
 * PLANTOWER G5
 */
public class PMS50003 extends BmobObject {
    /** pm 1.0 CF = 1 ug/m^3*/
    public Integer pm1_0_CF1;
    /** pm 2.5 CF = 1 ug/m^3*/
    public Integer pm2_5_CF1;
    /** pm 10 CF = 1 ug/m^3*/
    public Integer pm10_CF1;

    /** pm 1.0 china ug/m^3*/
    public Integer pm1_0;
    /** pm 2.5 china ug/m^3*/
    public Integer pm2_5;
    /** pm 10  china ug/m^3*/
    public Integer pm10;

    /** .3um in 0.1l */
    public Integer value_0_3;
    /** .5um in 0.1l */
    public Integer value_0_5;
    /** 1um in 0.1l */
    public Integer value_1;
    /** 2.5um in 0.1l */
    public Integer value_2_5;
    /** 5um in 0.1l */
    public Integer value_5;
    /** 10um in 0.1l */
    public Integer value_10;

    public Long recordedTime;

    public Integer reserve1;
    public Integer reserve2;
    public Integer reserve3;
    public Integer reserve4;
    public Integer reserve5;
    public Integer reserve6;


}
