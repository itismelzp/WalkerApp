package com.demo.logger

import android.content.Context
import android.os.Environment
import android.os.Looper.getMainLooper
import android.util.Log
import com.demo.BuildConfig
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.formatter.border.DefaultBorderFormatter
import com.elvishew.xlog.formatter.message.json.DefaultJsonFormatter
import com.elvishew.xlog.formatter.message.throwable.DefaultThrowableFormatter
import com.elvishew.xlog.formatter.message.xml.DefaultXmlFormatter
import com.elvishew.xlog.formatter.stacktrace.DefaultStackTraceFormatter
import com.elvishew.xlog.formatter.thread.DefaultThreadFormatter
import com.elvishew.xlog.interceptor.BlacklistTagsFilterInterceptor
import com.orhanobut.logger.Logger
import com.tencent.mars.sample.wrapper.remote.MarsServiceProxy
import com.tencent.mars.xlog.Xlog
import com.tencent.wink.storage.winkdb.log.WinkDbLog
import org.slf4j.LoggerFactory
import timber.log.Timber

/**
 * Created by lizhiping on 2022/12/18.
 * <p>
 * description
 */
class LogUtil {

    class DefaultLog : WinkDbLog.ILog {
        override fun v(tag: String, msg: String) {
            Log.v(tag, msg)
        }

        override fun d(tag: String, msg: String) {
            Log.d(tag, msg)
        }

        override fun i(tag: String, msg: String) {
            Log.i(tag, msg)
        }

        override fun w(tag: String, msg: String) {
            Log.w(tag, msg)
        }

        override fun w(tag: String, msg: String, t: Throwable) {
            Log.w(tag, msg, t)
        }

        override fun e(tag: String, msg: String) {
            Log.e(tag, msg)
        }

        override fun e(tag: String, msg: String, t: Throwable) {
            Log.e(tag, msg, t)
        }
    }

    /**
     * 格式好看，能直接定位log位置，不支持文件存储，只支持在IDE中查看日志；
     */
    class LoggerLog : MyLog.ILog {
        override fun v(tag: String?, msg: String?) {
            Logger.v("[%s] %s", tag, msg)
        }

        override fun d(tag: String?, msg: String?) {
            Logger.d("[%s] %s", tag, msg)
        }

        override fun i(tag: String?, msg: String?) {
            Logger.i("[%s] %s", tag, msg)
        }

        override fun w(tag: String?, msg: String?) {
            Logger.w("[%s] %s", tag, msg)
        }

        override fun e(tag: String?, msg: String?) {
            Logger.e("[%s] %s", tag, msg)
        }

        override fun e(tag: String?, msg: String?, t: Throwable?) {
            Logger.e(t, "[%s] %s", tag, msg)
        }
    }

    /**
     * 支持自定义TAG、日志格式、存储方式，自定义工作量较大
     */
    class TimberLog : MyLog.ILog {
        init {
            Timber.plant(Timber.DebugTree())
//        Timber.plant(FileLoggingTree())
        }

        override fun v(tag: String?, msg: String?) {
            Timber.tag(tag).v(msg)
        }

        override fun d(tag: String?, msg: String?) {
            Timber.tag(tag).d(msg)
        }

        override fun i(tag: String?, msg: String?) {
            Timber.tag(tag).i(msg)
        }

        override fun w(tag: String?, msg: String?) {
            Timber.tag(tag).w(msg)
        }

        override fun e(tag: String?, msg: String?) {
            Timber.tag(tag).e(msg)
        }

        override fun e(tag: String?, msg: String?, t: Throwable?) {
            Timber.tag(tag).e(t, msg)
        }
    }

    /**
     * 功能强大，可以根据策略文件配置日志打印格式、存储方式
     */
    class LogbackLog : MyLog.ILog {

        // 以下日志只会在控制台输出
        private val logger = LoggerFactory.getLogger("consoleLogger")

        // 以下日志会在BASE_ROLL_FILE声明的文件中输出，并且也会在控制台输出
        // 这里使用logback.xml中的logger name策略，也作为TAG
        val fileLogger: org.slf4j.Logger = LoggerFactory.getLogger("fileLogger")

        override fun v(tag: String?, msg: String?) {
            logger.trace("{}-->{}", tag, msg)
        }

        override fun d(tag: String?, msg: String?) {
            logger.debug("{}-->{}", tag, msg)
        }

        override fun i(tag: String?, msg: String?) {
            logger.info("{}-->{}", tag, msg)
        }

        override fun w(tag: String?, msg: String?) {
            logger.warn("{}-->{}", tag, msg)
        }

        override fun e(tag: String?, msg: String?) {
            logger.error("{}-->{}", tag, msg)
        }

        override fun e(tag: String?, msg: String?, t: Throwable?) {
            logger.error("{}-->{}", tag, msg)
        }

    }

    /**
     * Logger?+文件存储（IO流）
     */
    class XLogLog : MyLog.ILog {
        init {
            val config = LogConfiguration.Builder()
                .logLevel(
                    if (BuildConfig.DEBUG) LogLevel.ALL // Specify log level, logs below this level won't be printed, default: LogLevel.ALL
                    else LogLevel.NONE
                )
                .tag("MY_XLOG_TAG") // Specify TAG, default: "X-LOG"
                .enableThreadInfo() // Enable thread info, disabled by default
                .enableStackTrace(2) // Enable stack trace info with depth 2, disabled by default
                .enableBorder() // Enable border, disabled by default
                .jsonFormatter(DefaultJsonFormatter()) // Default: DefaultJsonFormatter
                .xmlFormatter(DefaultXmlFormatter()) // Default: DefaultXmlFormatter
                .throwableFormatter(DefaultThrowableFormatter()) // Default: DefaultThrowableFormatter
                .threadFormatter(DefaultThreadFormatter()) // Default: DefaultThreadFormatter
                .stackTraceFormatter(DefaultStackTraceFormatter()) // Default: DefaultStackTraceFormatter
                .borderFormatter(DefaultBorderFormatter()) // Default: DefaultBorderFormatter
//            .addObjectFormatter(
//                AnyClass::class.java,  // Add formatter for specific class of object
//                AnyClassObjectFormatter()
//            ) // Use Object.toString() by default
                .addInterceptor(
                    BlacklistTagsFilterInterceptor( // Add blacklist tags filter
                        "blacklist1", "blacklist2", "blacklist3"
                    )
                )
//            .addInterceptor(MyInterceptor()) // Add other log interceptor
                .build()
            XLog.init(config)
        }

        override fun v(tag: String?, msg: String?) {
            XLog.tag(tag).v(msg)
        }

        override fun d(tag: String?, msg: String?) {
            XLog.tag(tag).d(msg)
        }

        override fun i(tag: String?, msg: String?) {
            XLog.tag(tag).i(msg)
        }

        override fun w(tag: String?, msg: String?) {
            XLog.tag(tag).w(msg)
        }

        override fun e(tag: String?, msg: String?) {
            XLog.tag(tag).e(msg)
        }

        override fun e(tag: String?, msg: String?, t: Throwable?) {
            XLog.tag(tag).e(msg, t)
        }

    }

    /**
     * 日志格式固定（或重编so修改），存储文件效率最高（mmap方式），多级缓存，错误日志会单独存文件
     */
    class MarslogLog(context: Context) : MyLog.ILog {

        init {
            System.loadLibrary("c++_shared")
            System.loadLibrary("marsxlog")
            val sdcard: String = Environment.getExternalStorageDirectory().absolutePath
            val logPath: String = "$sdcard/marssample/log"

            // this is necessary, or may crash for SIGBUS
            val cachePath: String = "${context.filesDir.path}/xlog"

            val xlog = Xlog()
            MarsServiceProxy.init(context, getMainLooper(), null);
        }

        override fun v(tag: String?, msg: String?) {
            TODO("Not yet implemented")
        }

        override fun d(tag: String?, msg: String?) {
            TODO("Not yet implemented")
        }

        override fun i(tag: String?, msg: String?) {
            TODO("Not yet implemented")
        }

        override fun w(tag: String?, msg: String?) {
            TODO("Not yet implemented")
        }

        override fun e(tag: String?, msg: String?) {
            TODO("Not yet implemented")
        }

        override fun e(tag: String?, msg: String?, t: Throwable?) {
            TODO("Not yet implemented")
        }

    }
}