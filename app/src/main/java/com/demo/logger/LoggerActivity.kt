package com.demo.logger

import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.lifecycleScope
import com.demo.R
import com.demo.databinding.ActivityLoggerLayoutBinding
import com.demo.network.MediaFileMetaDataManager
import com.demo.network.model.*
import com.demo.utils.DeviceIdUtil
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader

class LoggerActivity : BaseActivity<ActivityLoggerLayoutBinding>() {

    private lateinit var logger: MyLog.ILog

    companion object {
        private const val TAG = "LoggerActivity"
    }




    override fun initBaseData(savedInstanceState: Bundle?) {
        super.initBaseData(savedInstanceState)
        requestPermission()
        initLog()
    }

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)
        binding.btnPing.setOnClickListener {
            lifecycleScope.launch {
                val ip = "10.250.13.125"
                toast("ping: $ip")
                val result = withContext(Dispatchers.IO) {
//                    ping("127.0.0.1")
                    ping(ip)
                }
                toastPintMsg(result)
            }
        }

        binding.btnDeviceId.text = getString(R.string.device_id, DeviceIdUtil.getDeviceId(this))

        binding.btnMediaMetaUpload.setOnClickListener {
            lifecycleScope.launch {
                val metaData = Gson().fromJson(DataCreator.META_DATA, MediaFileMetaDataRequest::class.java)
                MyLog.i(TAG, "metaData: ${Gson().toJson(metaData)}")
                MediaFileMetaDataManager.INSTANCE.uploadMetaData(metaData, object : Callback<MetaDataResponse> {
                    override fun onResponse(call: Call<MetaDataResponse>, response: Response<MetaDataResponse>) {
                        MyLog.d(TAG, "[onResponse] code: ${response.code()}")
                        toast("[onResponse] code: ${response.code()}")
                    }

                    override fun onFailure(call: Call<MetaDataResponse>, t: Throwable) {
                        MyLog.d(TAG, "[onFailure] t: $t")
                        toast("[onFailure] t: $t")
                    }
                })
            }
        }

        binding.btnFaceMetaUpload.setOnClickListener {
            lifecycleScope.launch {
                val metaData = Gson().fromJson(DataCreator.FACE_DATA, FaceScanMetaDataRequest::class.java)
                MyLog.i(TAG, "metaData: ${Gson().toJson(metaData)}")
                MediaFileMetaDataManager.INSTANCE.uploadMetaData(metaData, object : Callback<MetaDataResponse> {
                    override fun onResponse(call: Call<MetaDataResponse>, response: Response<MetaDataResponse>) {
                        MyLog.d(TAG, "[onResponse] code: ${response.code()}")
                        toast("[onResponse] code: ${response.code()}")
                    }

                    override fun onFailure(call: Call<MetaDataResponse>, t: Throwable) {
                        MyLog.d(TAG, "[onFailure] t: $t")
                        toast("[onFailure] t: $t")
                    }
                })
            }
        }

        MyLog.i(TAG, "deviceId: ${DeviceIdUtil.getDeviceId(this)}")
    }

    override fun getViewBinding() = ActivityLoggerLayoutBinding.inflate(layoutInflater)

    @UiThread
    private fun toastPintMsg(msg: String) {
        toast(msg)
    }

    /**
     * Ping命令格式为：ping -c 1 -w 5 ip
     * 其中参数 ：
     * -c：是指ping的次数
     * -w：是指执行的最后期限，单位为秒
     */
    @WorkerThread
    private fun ping(ip: String): String {

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
        return output.toString()
    }

    private fun appendLine(sb: StringBuffer?, line: String) {
        sb?.append("$line\n")
    }

    private fun initLog() {
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
    }

    private fun initLogger(canUse: Boolean = true) {
        if (!canUse) {
            return
        }
        logger = LogUtil.LoggerLog()
        logger.i(TAG, "Logger info level.")
        logger.d(TAG, "Logger debug level.")
        logger.w(TAG, "Logger warning level.")
        logger.e(TAG, "Logger error level.")
    }

    private fun initTimber(canUse: Boolean = true) {
        if (!canUse) {
            return
        }

        logger = LogUtil.TimberLog()
        logger.i(TAG, "Timber warning level.")
        logger.d(TAG, "Timber warning level.")
        logger.w(TAG, "Timber warning level.")
        logger.e(TAG, "Timber %s", Exception("some error"))
    }

    private fun initLogback(canUse: Boolean = true) {
        if (!canUse) {
            return
        }
        logger = LogUtil.LogbackLog()
        logger.i(TAG, "info")
        logger.v(TAG, "trace")
        logger.d(TAG, "debug")
        logger.w(TAG, "warn")
        logger.e(TAG, "error")
    }

    private fun initXLog(canUse: Boolean = true) {
        if (!canUse) {
            return
        }
        logger = LogUtil.XLogLog()
        logger.i(TAG, "XLog info level.")
        logger.d(TAG, "XLog debug level.")
        logger.w(TAG, "XLog warn level.")
        logger.e(TAG, "XLog error level.", Exception("some error"))
    }

    private fun initMarslog(canUse: Boolean = true) {
        if (!canUse) {
            return
        }
        logger = LogUtil.MarslogLog(this)
    }
}