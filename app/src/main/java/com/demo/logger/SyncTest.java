package com.demo.logger;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

import com.demo.base.log.MyLog;
import com.demo.utils.DataFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by lizhiping on 2023/3/21.
 * <p>
 * description
 */
public class SyncTest implements Serializable {

    private static final String TAG = "SyncTest";
    private Handler handler;
    private final static int MSG_A = 1;
    private final static int MSG_B = 2;
    private final static int MSG_C = 3;


    public void initHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("downloadImage");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                MyLog.i(TAG, "[handleMessage] msg: " + msg.what);
                switch (msg.what) {
                    case MSG_A:
                        handA(msg);
                        break;
                    case MSG_B:
                        handB(msg);
                    case MSG_C:
                        handC();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void handA(Message msg) {
        Result result = (Result) msg.obj;
        result.doSomething();
    }

    private void handB(Message msg) {
        Result result = (Result) msg.obj;
        result.doSomething();
    }

    private void handC() {
        Executor executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);
        executor.execute(() -> {
//            sendMsgA(new Result(MSG_A, latch));
            new Result(MSG_A, latch).doSomething();
        });
        executor.execute(() -> {
//            sendMsgA();
            new Result(MSG_B, latch).doSomething();
        });
        ResultC resultC = new ResultC(latch);
        resultC.doSomething();
    }

    public void sendMsgC() {
        handler.obtainMessage(MSG_C).sendToTarget();
    }

    public void sendMsgA(Result result) {
        handler.obtainMessage(MSG_A, result).sendToTarget();
    }

    public void sendMsgB(Result result) {
        handler.obtainMessage(MSG_B, result).sendToTarget();
    }

    static class Result {

        private int what;
        private CountDownLatch latch;

        public Result(int what, CountDownLatch latch) {
            this.what = what;
            this.latch = latch;
        }

        public void doSomething() {
            try {
                MyLog.i(TAG, "[doSomething] what:" + what + ", START");
                TimeUnit.MILLISECONDS.sleep(2_000L * what);
                latch.countDown();
                MyLog.i(TAG, "[doSomething] what:" + what + ", countDown");
            } catch (InterruptedException e) {

            }
        }
    }

    class ResultC {

        private int what;
        private CountDownLatch latch;

        public ResultC(CountDownLatch latch) {
            this.what = MSG_C;
            this.latch = latch;
        }

        public void doSomething() {
            MyLog.i(TAG, "[doSomething] what:" + what + ", WAITING...");
            try {
                latch.await();
            } catch (Exception e) {

            }
            MyLog.i(TAG, "[doSomething] what:" + what + ", FINISHED.");
        }

    }

    public void startTest() {

        List<Integer> localData = new ArrayList<>();
        List<Integer> cloudData = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(2_000);
                localData.addAll(DataFactory.getEven(10));
                latch.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(2_000);
                cloudData.addAll(DataFactory.getOdd(10));
                latch.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        try {
            latch.await();
            localData.addAll(cloudData);
            MyLog.i(TAG, "all Data: " + localData);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}



