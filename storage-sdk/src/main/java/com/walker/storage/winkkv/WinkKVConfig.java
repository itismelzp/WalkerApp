package com.walker.storage.winkkv;

import com.walker.storage.winkkv.log.WinkKVLog;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * WinkKV的配置类，如：配置日志工具、线程池
 * <p>
 * Created by walkerzpli on 2021/10/15.
 */
public final class WinkKVConfig {

    static WinkKVLog mLogger;
    static volatile Executor sExecutor;

    private WinkKVConfig() {
    }

    public static void initLogger(WinkKVLog logger) {
        mLogger = logger;
    }

    /**
     * 推荐使用公共的线程池
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
