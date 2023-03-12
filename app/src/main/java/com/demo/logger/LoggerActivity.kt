package com.demo.logger

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.lifecycleScope
import com.demo.R
import com.demo.databinding.ActivityLoggerLayoutBinding
import com.demo.network.RequestAccessManager
import com.demo.network.model.DataCreator
import com.demo.network.model.FaceScanMetaDataRequest
import com.demo.network.model.MediaFileMetaDataRequest
import com.demo.network.model.MetaDataResponse
import com.demo.network.model.Person
import com.demo.network.model.SearchRequest
import com.demo.network.model.SearchResultResponse
import com.demo.network.model.TestData
import com.demo.network.utils.DataConverter
import com.demo.network.utils.GsonUtil
import com.demo.utils.DeviceIdUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type

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
                toast(result)
            }
        }

        binding.btnDeviceId.text = getString(R.string.device_id, DeviceIdUtil.getDeviceId(this))

        binding.btnMediaMetaUpload.setOnClickListener {
            lifecycleScope.launch {
                val metaData = Gson().fromJson(DataCreator.MEDIA_META_DATA, MediaFileMetaDataRequest::class.java)
                metaData.mediaFileMetaDatas[0].latitude = Double.NaN
                if (metaData.mediaFileMetaDatas[0].latitude!!.isNaN()) {
//                    metaData.mediaFileMetaDatas[0].latitude = null
                }
                MyLog.i(TAG, "metaData: ${GsonUtil.getGson().toJson(metaData)}")
                RequestAccessManager.INSTANCE.uploadMetaData(metaData, object : Callback<MetaDataResponse> {
                    override fun onResponse(call: Call<MetaDataResponse>, response: Response<MetaDataResponse>) {
                        MyLog.d(TAG, "[onResponse] code: ${response.code()}, data: ${response.body()}")
                        toast("[onResponse] code: ${response.code()}, data: ${response.body()}")
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
                val metaData = Gson().fromJson(DataCreator.FACE_META_DATA, FaceScanMetaDataRequest::class.java)
                MyLog.i(TAG, "metaData: ${Gson().toJson(metaData)}")
                toast("metaData: ${Gson().toJson(metaData)}")
                RequestAccessManager.INSTANCE.uploadMetaData(metaData, object : Callback<MetaDataResponse> {
                    override fun onResponse(call: Call<MetaDataResponse>, response: Response<MetaDataResponse>) {
                        MyLog.d(TAG, "[onResponse] code: ${response.code()}, data: ${response.body()}")
                        toast("[onResponse] code: ${response.code()}, data: ${response.body()}")
                    }

                    override fun onFailure(call: Call<MetaDataResponse>, t: Throwable) {
                        MyLog.d(TAG, "[onFailure] t: $t")
                        toast("[onFailure] t: $t")
                    }
                })
            }
        }

        binding.btnSearchResultData.setOnClickListener {

            MyLog.i(TAG, "SEARCH_SIMPLE_RESULT_DATA: ${DataCreator.SEARCH_SIMPLE_RESULT_DATA}")
            val searchResultResponse = Gson().fromJson(DataCreator.SEARCH_SIMPLE_RESULT_DATA, SearchResultResponse::class.java)
            val dataList = searchResultResponse.aggregations.data
            binding.resultTv.text = "SEARCH_SIMPLE_RESULT_DATA: ${DataCreator.SEARCH_SIMPLE_RESULT_DATA}"
            MyLog.i(TAG, "dataList.size: ${dataList.size}, searchResultResponse: $searchResultResponse")


            val mediaItemList = DataConverter.dataConvert(dataList)
            MyLog.i(TAG, "size: ${mediaItemList.size}, mediaItemList: $mediaItemList")

            val searchBtn = binding.searchEt
            val request = SearchRequest(
                if (!TextUtils.isEmpty(searchBtn.text.toString()))
                    searchBtn.text.toString()
                else
                    searchBtn.hint.toString(),
                2,
                "all"
            )

            RequestAccessManager.INSTANCE.search(request, object : Callback<SearchResultResponse> {
                override fun onResponse(call: Call<SearchResultResponse>, response: Response<SearchResultResponse>) {

                    val searchResultResponse = response.body()
                    val dataSize = searchResultResponse?.aggregations?.data?.size ?: 0

                    val result = "【request: $request】\n[onResponse] code: ${response.code()} size: $dataSize\ndata: $searchResultResponse"
                    MyLog.d(TAG, result)
//                    toast(result)
                    binding.resultTv.text = result
                }

                override fun onFailure(call: Call<SearchResultResponse>, t: Throwable) {
                    val result ="[onFailure] t: $t"
                    MyLog.e(TAG, result)
//                    toast(result)
                    binding.resultTv.text = result
                }
            })
        }

        binding.btnCoroutineSearch.setOnClickListener {
//            MyLog.i(TAG, "SEARCH_RESULT_DATA: ${DataCreator.SEARCH_RESULT_DATA}")
//            val responseJason =
//                Gson().fromJson(DataCreator.SEARCH_RESULT_DATA, SearchResultResponse::class.java)
//            MyLog.i(TAG, "searchResultResponse: $responseJason")

            val searchBtn = binding.btnCoroutineSearch
            val request = SearchRequest(
                if (!TextUtils.isEmpty(searchBtn.text.toString()))
                    searchBtn.text.toString()
                else
                    searchBtn.hint.toString(),
                2000,
                "all"
            )
            mainScope.launch {
                val result = withContext(Dispatchers.IO) {
                    val searchResultResponse =
                        RequestAccessManager.INSTANCE.coroutineSearch(request)
                    val dataSize = searchResultResponse.aggregations.data.size
                    return@withContext "【request: $request】\n[onResponse] size: $dataSize\ndata: $searchResultResponse"
                }

                MyLog.d(TAG, result)
                binding.resultTv.text = result
            }
        }

        binding.btnTestData.setOnClickListener {
            val testdata = Gson().fromJson(DataCreator.TEST_DATA, TestData::class.java)
            MyLog.i(TAG, "testdata: $testdata")

            val data = testdata.data
            val type: Type = object : TypeToken<Map<String, Person>>() {}.type
            val map: Map<String, Person> = Gson().fromJson(data, type)
            MyLog.i(TAG, "map: $map")

            map.forEach { (digit, person) -> MyLog.i(TAG, "$digit: $person") }
        }

        MyLog.i(TAG, "deviceId: ${DeviceIdUtil.getDeviceId(this)}")
    }

    override fun getViewBinding() = ActivityLoggerLayoutBinding.inflate(layoutInflater)

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