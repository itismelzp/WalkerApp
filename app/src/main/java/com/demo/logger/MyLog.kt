package com.demo.logger

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

        fun v(tag: String?, msg: String?) {
            if (sLogInf != null) {
                sLogInf!!.v(tag, msg)
            }
        }

        fun d(tag: String?, msg: String?) {
            if (sLogInf != null) {
                sLogInf!!.d(tag, msg)
            }
        }

        fun i(tag: String?, msg: String?) {
            if (sLogInf != null) {
                sLogInf!!.i(tag, msg)
            }
        }

        fun w(tag: String?, msg: String?) {
            if (sLogInf != null) {
                sLogInf!!.w(tag, msg)
            }
        }

        fun e(tag: String?, msg: String?) {
            if (sLogInf != null) {
                sLogInf!!.e(tag, msg)
            }
        }

        fun e(tag: String?, msg: String?, t: Throwable?) {
            if (sLogInf != null) {
                sLogInf!!.e(tag, msg, t)
            }
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