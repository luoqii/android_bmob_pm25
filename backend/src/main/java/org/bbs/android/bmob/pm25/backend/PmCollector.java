package org.bbs.android.bmob.pm25.backend;

import android.util.Log;

import org.bbs.android.pm25.library.PMS50003;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bysong on 16-3-22.
 */
public class PmCollector {
    private static final String TAG = PmCollector.class.getSimpleName();
    private static final boolean DEBUG = true;

    private static PmCollector sInstance;

    public static final int DATA_LENGTH = 28;
    byte rawData[] = new byte[32];

    public static byte C_0X42 = 4 * 16 + 2 * 1;
    public static byte C_0X4D = 4 * 16 + 13 * 1;
    byte mIndex = -1;
    byte mCurrentData;
    byte mLastData;
    private PMS50003 mLastPm;
    private List<PmCallback> mCallbacks;

    public static PmCollector getInstance(){
        if (null == sInstance){
            sInstance = new PmCollector();
        }

        return sInstance;
    }

    private PmCollector(){
        mLastPm = new PMS50003();
        rawData[0] = C_0X42;
        rawData[1] = C_0X4D;
    }

    public void onDataRcvd(byte[] bytes){
        for (byte b : bytes){
            onDataRcvd(b);
        }
    }

    public void onDataRcvd(byte b){
        if (DEBUG) {
//            Log.d(TAG, "onDataRcvd:0x" + Integer.toString(b, 16) + " mIndex:" + mIndex);
        }
        mLastData = mCurrentData;
        mCurrentData = b;

        if (mLastData == C_0X42 && mCurrentData == C_0X4D) {
            mIndex = 1;
        }

        if (mIndex > 0 && mIndex < DATA_LENGTH){
            rawData[mIndex] = b;

            if (mIndex == DATA_LENGTH - 1){
                onPmAvaiable();
                mIndex = -1;
            } else {
                mIndex++;
            }
        }
    }

    private void onPmAvaiable() {
        // we must create an NEW pm for bmob.
        mLastPm = new PMS50003();
        mLastPm.pm1_0_CF1 = getData(4);
        mLastPm.pm2_5_CF1 = getData(6);
        mLastPm.pm10_CF1  = getData(8);

        mLastPm.pm1_0     = getData(10);
        mLastPm.pm2_5     = getData(12);
        mLastPm.pm10      = getData(14);

        mLastPm.value_0_3 = getData(16);
        mLastPm.value_0_5 = getData(18);
        mLastPm.value_1   = getData(20);
        mLastPm.value_2_5 = getData(22);
        mLastPm.value_5   = getData(24);
        mLastPm.value_10  = getData(26);

        mLastPm.recordedTime = System.currentTimeMillis();

        if (DEBUG){
            String data = "0x";
            for (int i = 0 ; i < DATA_LENGTH ; i++){
                data += byte2String(rawData[i]) + " ";
                data += (i % 2 == 1) ? "  " : "";
            }
            Log.d(TAG, "data: " + data);
            Log.d(TAG, "new pm availiable. pm:" + mLastPm);
        }
        pmAvaiable(mLastPm);

//        Realm_PMS50003 r = Realm_PMS50003.fromPm(mLastPm);
//        realm.beginTransaction();
//        Realm_PMS50003 rPm = realm.copyToRealm(r);
//        realm.commitTransaction();

//        Log.d(TAG, "save mLastPm: " + mLastPm);
    }

    public void pmAvaiable(PMS50003 pm){
        if (mCallbacks != null && mCallbacks.size() > 0){
            for (PmCallback c : mCallbacks){
                c.onPmAvailable(pm);
            }
        }
    }

    public String byte2String(byte b){
        String str = Integer.toHexString(b);
        if (str.length() == 8) {
            str = str.substring(6);
        }
        str = str.toUpperCase();

//        str = "";
//        for (int power = 7 ; power >= 0 ; power--){
//            str += (b / ((int)Math.pow(2, power)));
//            b = (byte) (b % ((int)Math.pow(2, power)));
//        }

        return str;
    }

    public PMS50003 getmLastPm(){
        return mLastPm;
    }

    // debug only
    public void resetLastPm(){
        mLastPm = new PMS50003();
    }

    int getData(int startIndex) {
        int data = -1;
        byte h = rawData[startIndex];
        byte l = rawData[startIndex + 1];

        int hInt = unsignedByte(h) * 256;
        int lInt = unsignedByte(l);
        data = hInt + lInt;
        return data;
    }

    int unsignedByte(byte b){
        return 0x00ff & b;
    }

    public void addCallback(PmCallback c){
        if (mCallbacks == null){
            mCallbacks = new ArrayList<>();
        }
        mCallbacks.add(c);
    }

    public static interface PmCallback {
        public void onPmAvailable(PMS50003 pm);
    }
}
