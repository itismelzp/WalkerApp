package com.demo.base.log

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by lizhiping on 2022/12/18.
 * <p>
 * description
 */
class MyLog {

    companion object {

        @Volatile
        private var sLogInf: ILog? = null
        private val mInitialized = AtomicBoolean(false)

        @JvmStatic
        fun init(logInf: ILog?) {
            if (logInf == null || !mInitialized.compareAndSet(false, true)) {
                return
            }
            sLogInf = logInf
        }

        @JvmStatic
        fun v(tag: String?, msg: String?) {
            sLogInf?.apply { v(tag, msg) }
        }

        @JvmStatic
        fun d(tag: String?, msg: String?) {
            sLogInf?.apply { d(tag, msg) }
        }

        @JvmStatic
        fun i(tag: String?, msg: String?) {
            sLogInf?.apply { i(tag, msg) }
        }

        @JvmStatic
        fun w(tag: String?, msg: String?) {
            sLogInf?.apply { w(tag, msg) }
        }

        @JvmStatic
        fun e(tag: String?, msg: String?) {
            sLogInf?.apply { e(tag, msg) }
        }

        @JvmStatic
        fun e(tag: String?, msg: String?, t: Throwable?) {
            sLogInf?.apply { e(tag, msg, t) }
        }

        @JvmStatic
        fun getTimeCost(time: Long): Long {
            return System.currentTimeMillis() - time
        }
    }

    /**
     * 外部设置的日志api
     */
    interface ILog {
        fun v(tag: String?, msg: String?)
        fun d(tag: String?, msg: String?)
        fun i(tag: String?, msg: String?)
        fun w(tag: String?, msg: String?)
        fun e(tag: String?, msg: String?)
        fun e(tag: String?, msg: String?, t: Throwable?)
    }

}