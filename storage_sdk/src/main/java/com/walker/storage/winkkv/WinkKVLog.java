package com.walker.storage.winkkv;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by walkerzpli on 2021/10/18.
 */
public class WinkKVLog {

    private static volatile WinkDbLogInterface sLogInf;
    private static final AtomicBoolean INIT = new AtomicBoolean(false);

    public static void init(WinkDbLogInterface logInf) {
        if (logInf == null || !INIT.compareAndSet(false, true)) {
            return;
        }
        sLogInf = logInf;
    }

    public static void v(String tag, String msg) {
        if (sLogInf != null) {
            sLogInf.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (sLogInf != null) {
            sLogInf.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sLogInf != null) {
            sLogInf.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (sLogInf != null) {
            sLogInf.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable t) {
        if (sLogInf != null) {
            sLogInf.w(tag, msg, t);
        }
    }

    public static void e(String tag, String msg) {
        if (sLogInf != null) {
            sLogInf.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable t) {
        if (sLogInf != null) {
            sLogInf.e(tag, msg, t);
        }
    }

    /**
     * 外部设置的日志api
     */
    public interface WinkDbLogInterface {

        void v(String tag, String msg);

        void d(String tag, String msg);

        void i(String tag, String msg);

        void w(String tag, String msg);

        void w(String tag, String msg, Throwable t);

        void e(String tag, String msg);

        void e(String tag, String msg, Throwable t);
    }
}
