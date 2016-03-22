package org.bbs.android.bmob.pm25.backend;

import org.bbs.android.pm25.library.PMS50003;

/**
 * Created by bysong on 16-3-22.
 */
public class PmCollector {
    private static final String TAG = PmCollector.class.getSimpleName();

    private static PmCollector sInstance;

    // keep sync with rawData's length
    public static final int DATA_LENGTH = 2 + 2 + 13 * 2 + 2;
    byte rawData[] = new byte[32];

    public static byte C_0X42 = 4 * 16 + 2 * 1;
    public static byte C_0X4D = 4 * 16 + 13 * 1;
    byte index = -1;
    byte currentData;
    byte lastData;
    private PMS50003 lastPm;
    private PmCallback mCallback;

    public static PmCollector getInstance(){
        if (null == sInstance){
            sInstance = new PmCollector();
        }

        return sInstance;
    }

    private PmCollector(){
        lastPm = new PMS50003();
    }

    public void onDataRcvd(byte[] bytes){
        for (byte b : bytes){
            onDataRcvd(b);
        }
    }

    public void onDataRcvd(byte b){
        lastData = currentData;
        currentData = b;

        if (lastData == C_0X42 && currentData == C_0X4D) {
            //    Serial.print("new data avaiable.");
            index = 1;
        }

        if (index >= 1 && index < DATA_LENGTH) {
            rawData[index] = currentData;
            index++;
        } else if (index == DATA_LENGTH) {
//            showData();
            index = -1;
            trySaveAndUpload();
        }

        //    Serial.print(c,HEX);
        //    Serial.write(mySerial.read());
    }

    private void trySaveAndUpload() {
        lastPm.pm10_CF1 = getData(4);
        lastPm.pm2_5_CF1= getData(6);
        lastPm.pm10_CF1 = getData(8);

        lastPm.pm10     = getData(10);
        lastPm.pm2_5    = getData(12);
        lastPm.pm10     = getData(16);

        lastPm.value_0_3 = getData(16);
        lastPm.value_0_5 = getData(18);
        lastPm.value_1   = getData(20);
        lastPm.value_2_5 = getData(22);
        lastPm.value_5   = getData(24);
        lastPm.value_10  = getData(26);

        if (mCallback != null){
            mCallback.onPmAvailable(lastPm);
        }
//        Realm_PMS50003 r = Realm_PMS50003.fromPm(lastPm);
//        realm.beginTransaction();
//        Realm_PMS50003 rPm = realm.copyToRealm(r);
//        realm.commitTransaction();

//        Log.d(TAG, "save lastPm: " + lastPm);
    }

    public PMS50003 getLastPm(){
        return lastPm;
    }

    // debug only
    public void resetLastPm(){
        lastPm = new PMS50003();
    }

    int getData(int startIndex) {
        int data = -1;
        byte h = rawData[startIndex];
        byte l = rawData[startIndex + 1];
        data = h * 256 + l;
        return data;
    }

    public void setCallback(PmCallback c){
        mCallback = c;
    }

    public static interface PmCallback {
        public void onPmAvailable(PMS50003 pm);
    }
}
