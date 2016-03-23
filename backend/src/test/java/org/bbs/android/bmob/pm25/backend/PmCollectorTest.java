package org.bbs.android.bmob.pm25.backend;

import junit.framework.TestCase;

import org.bbs.android.pm25.library.PMS50003;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created by bysong on 16-3-22.
 */
public class PmCollectorTest extends TestCase {

    private static final String TAG = PmCollector.class.getSimpleName();
    public static final int RUN_COUNT = 1000000;
    private PmCollector mCollector;
    private Random mRandom;
    private PMS50003 tmpPm;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCollector = PmCollector.getInstance();
        mCollector.resetLastPm();
        mRandom = new Random();

        tmpPm = null;
        mCollector.setCallback(new PmCollector.PmCallback() {
            @Override
            public void onPmAvailable(PMS50003 pm) {
                tmpPm = pm;
            }
        });
    }

    public void test_no_pm(){
        assertTrue("tmpPm is not null", tmpPm == null);
        for (int i = 0 ; i < RUN_COUNT ; i++){
            logD(TAG, "test_no_pm. run: " + i);
            do_test_no_pm();
        }
    }

    public void do_test_no_pm(){
        tmpPm = null;

        ByteBuffer bBuffer = ByteBuffer.allocate(40);
        for (int round = mRandom.nextInt(40); round > 0 ; round--) {
            byte b = nextValidValue();
            mCollector.onDataRcvd(b);
            bBuffer.put(b);
        }

        assertTrue("has a pm with data:" + bufferString(bBuffer), tmpPm == null);
    }

    byte nextValidValue(){
        byte b;
        do {
            b = (byte) mRandom.nextInt();
        } while (b == PmCollector.C_0X42 || b == PmCollector.C_0X4D);

        return b;
    }

    public void test_pm(){
        assertTrue("tmpPm is not null", tmpPm == null);
        for (int i = 0 ; i < RUN_COUNT; i ++){
            logD(TAG, "test_pm. run: " + i);
            mCollector.resetLastPm();
            do_test_pm();
        }
    }

    void do_test_pm(){
        tmpPm = null;

        ByteBuffer bBuffer = ByteBuffer.allocate(500);
        for (int i = mRandom.nextInt(40) + PmCollector.DATA_LENGTH * 2; i > 0; i--) {
            byte b = nextValidValue();
            mCollector.onDataRcvd(b);
            bBuffer.put(b);
        }

        // start bytes
        mCollector.onDataRcvd(PmCollector.C_0X42);
        bBuffer.put(PmCollector.C_0X42);
        mCollector.onDataRcvd(PmCollector.C_0X4D);
        bBuffer.put(PmCollector.C_0X4D);

        for (int i = mRandom.nextInt(40) + PmCollector.DATA_LENGTH*2; i > 0 ; i--){
            byte b = nextValidValue();
            mCollector.onDataRcvd(b);
            bBuffer.put(b);
        }

        PMS50003 pm = mCollector.getLastPm();
        assertTrue("has not a pm with data:" + bufferString(bBuffer), tmpPm != null);
    }

    String bufferString(ByteBuffer buffer) {
        int position = buffer.position();
        String str = "buffer:";
        for (int i = 0; i < position; i++) {
            str += " " + Integer.toString(buffer.get(i), 16) + "[" + buffer.get(i) + "]";
        }

        return str;
    }

    void logD(String tag, String message){
//        System.out.print(tag + ":" + message + "\n");
    }
}
