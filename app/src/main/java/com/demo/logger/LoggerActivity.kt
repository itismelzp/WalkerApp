package com.demo.logger

import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.demo.BuildConfig
import com.demo.R
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
import org.slf4j.LoggerFactory
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader


class LoggerActivity : BaseActivity() {

    companion object {
        private const val TAG = "LoggerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logger_layout)

        requestPermission()

        // Logger -- TAG为：PRETTYLOGGER
        initLogger(false)

        // Timber -- 可自定义TAG
        initTimber(false)

        // logback-android
        initLogback(false)

        // XLog -- TAG为：X-LOG
        initXLog(false)

        // mars-xlog -- 文件存储效率高
        initMarslog(false)


        ping("10.250.13.125")
        ping("10.250.12.155")
    }

    /**
     * Ping命令格式为：ping -c 1 -w 5 ip
     * 其中参数 ：
     * -c：是指ping的次数
     * -w：是指执行的最后期限，单位为秒
     */
    private fun ping(ip: String) {

        val cmd = "ping -c 1 -w 4 $ip"
        Log.i(TAG, "ping ip: $ip")
        val process = Runtime.getRuntime().exec(cmd)

        val br: BufferedReader?
        val output = StringBuffer()

        br = BufferedReader(InputStreamReader(process.inputStream))
        while (true) {
            val line: String = br.readLine() ?: break
            appendLine(output, line)
        }

        val status = process.waitFor()
        val msg = if (status == 0) {
            appendLine(output, "exec cmd success:$cmd");
            "ping ip: $ip, status: 0, result: success."
        } else {
            appendLine(output, "exec cmd fail.");
            "ping ip: $ip, status: $status, result: failed."
        }
//        toast(msg)
        toast(output.toString())
    }

    private fun appendLine(sb: StringBuffer?, line: String) {
        sb?.append("$line\n")
    }

    /**
     * 格式好看，能直接定位log位置，不支持文件存储，只支持在IDE中查看日志；
     */
    private fun initLogger(canUse: Boolean = true) {
        if (!canUse) {
            return
        }

        Logger.i("Logger info level.")
        Logger.d("Logger debug level.")
        Logger.w("Logger warning level.")
        Logger.e("Logger error level.")
    }

    /**
     * 支持自定义TAG、日志格式、存储方式，自定义工作量较大
     */
    private fun initTimber(canUse: Boolean = true) {
        if (!canUse) {
            return
        }

        Timber.plant(Timber.DebugTree())
//        Timber.plant(FileLoggingTree())

        Timber.tag("TIMER").i("Timer info level.")
        Timber.d("Timber %d", 10)
        Timber.w("Timber %s", "warning level.")
        Timber.e("Timber %s", Exception("some error").message)
    }

    /**
     * 功能强大， 可以根据策略文件配置日志打印格式、存储方式
     */
    private fun initLogback(canUse: Boolean = true) {
        if (!canUse) {
            return
        }

        // 以下日志只会在控制台输出
        val consoleLogger = LoggerFactory.getLogger("consoleLogger")
        consoleLogger.trace("logback-->{}", "trace")
        consoleLogger.debug("logback-->{}", "debug")
        consoleLogger.info("logback-->{}", "info")
        consoleLogger.warn("logback-->{}", "warn")
        consoleLogger.error("logback-->{}", "error")

        // 以下日志会在BASE_ROLL_FILE声明的文件中输出，并且也会在控制台输出
        // 这里使用logback.xml中的logger name策略，也作为TAG
        val fileLogger: org.slf4j.Logger = LoggerFactory.getLogger("logtest")
        fileLogger.trace("trace-->{}", "trace")
        fileLogger.debug("logback-->{}", "debug")
        fileLogger.info("logback-->{}", "info")
        fileLogger.warn("logback-->{}", "warn")
        fileLogger.error("logback-->{}", "error")
    }

    /**
     * Logger?+文件存储（IO流）
     */
    private fun initXLog(canUse: Boolean = true) {
        if (!canUse) {
            return
        }

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
        XLog.i("XLog info level.")
        XLog.d("XLog debug level.")
        XLog.w("XLog warn level.")
        XLog.e("XLog error level.", Exception("some error").message)
    }

    /**
     * 日志格式固定（或重编so修改），存储文件效率最高（mmap方式），多级缓存，错误日志会单独存文件
     */
    private fun initMarslog(canUse: Boolean = true) {
        if (!canUse) {
            return
        }
        System.loadLibrary("c++_shared")
        System.loadLibrary("marsxlog")
        val sdcard: String = Environment.getExternalStorageDirectory().absolutePath
        val logPath: String = "$sdcard/marssample/log"

        // this is necessary, or may crash for SIGBUS
        val cachePath: String = "${this.filesDir.path}/xlog"

//        val xlog = Xlog()
//        MarsServiceProxy.init(this, getMainLooper(), null);
    }
}