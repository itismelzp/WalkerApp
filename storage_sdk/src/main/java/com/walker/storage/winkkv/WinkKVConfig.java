package com.walker.storage.winkkv;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by walkerzpli on 2021/10/15.
 */
public final class WinkKVConfig {
    static WinkKV.Logger sLogger;
    static volatile Executor sExecutor;

    private WinkKVConfig() {
    }

    public static void setLogger(WinkKV.Logger logger) {
        sLogger = logger;
    }

    /**
     * It's highly recommended to set your own Executor for reusing threads in common thread pool.
     *
     * @param executor The executor for loading or writing.
     */
    public static void setExecutor(Executor executor) {
        if (executor != null) {
            sExecutor = executor;
        }
    }

    static Executor getExecutor() {
        if (sExecutor == null) {
            synchronized (WinkKVConfig.class) {
                if (sExecutor == null) {
                    ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 4,
                            30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
                    executor.allowCoreThreadTimeOut(true);
                    sExecutor = executor;
                }
            }
        }
        return sExecutor;
    }
}
