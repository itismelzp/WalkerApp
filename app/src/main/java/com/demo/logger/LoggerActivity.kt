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
import com.demo.network.model.MediaPath
import com.demo.network.model.SearchMediaItem
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

        binding.btnDeviceId.text = getString(R.string.device_id, DeviceIdUtil.getDeviceId(this))
        MyLog.i(TAG, "deviceId: ${DeviceIdUtil.getDeviceId(this)}")

        initPingView()
        initMetaUploadView()
        initSearchView()

    }

    private fun initPingView() {
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
    }

    private fun initMetaUploadView() {
        binding.btnMediaMetaUpload.setOnClickListener {
            lifecycleScope.launch {
                val metaData = Gson().fromJson(
                    DataCreator.MEDIA_META_DATA,
                    MediaFileMetaDataRequest::class.java
                )
//                metaData.mediaFileMetaDatas[0].latitude = Double.NaN
                if (metaData.mediaFileMetaDatas[0].latitude!!.isNaN()) {
//                    metaData.mediaFileMetaDatas[0].latitude = null
                }
                MyLog.i(TAG, "metaData: ${GsonUtil.getGson().toJson(metaData)}")
                RequestAccessManager.INSTANCE.uploadMetaData(
                    metaData,
                    object : Callback<MetaDataResponse> {
                        override fun onResponse(
                            call: Call<MetaDataResponse>,
                            response: Response<MetaDataResponse>
                        ) {
                            MyLog.d(
                                TAG,
                                "[onResponse] code: ${response.code()}, data: ${response.body()}"
                            )
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
                val metaData =
                    Gson().fromJson(DataCreator.FACE_META_DATA, FaceScanMetaDataRequest::class.java)
                MyLog.i(TAG, "metaData: ${Gson().toJson(metaData)}")
                toast("metaData: ${Gson().toJson(metaData)}")
                RequestAccessManager.INSTANCE.uploadMetaData(
                    metaData,
                    object : Callback<MetaDataResponse> {
                        override fun onResponse(
                            call: Call<MetaDataResponse>,
                            response: Response<MetaDataResponse>
                        ) {
                            MyLog.d(
                                TAG,
                                "[onResponse] code: ${response.code()}, data: ${response.body()}"
                            )
                            toast("[onResponse] code: ${response.code()}, data: ${response.body()}")
                        }

                        override fun onFailure(call: Call<MetaDataResponse>, t: Throwable) {
                            MyLog.d(TAG, "[onFailure] t: $t")
                            toast("[onFailure] t: $t")
                        }
                    })
            }
        }
    }

    private fun getQuery1(): String {
        val searchEt = binding.searchEt
        return if (!TextUtils.isEmpty(searchEt.text.toString())) {
            searchEt.text.toString()
        } else {
            searchEt.hint.toString()
        }
    }

    private fun getQuery2(): String {
        val searchEt = binding.coroutineSearchEt
        return if (!TextUtils.isEmpty(searchEt.text.toString())) {
            searchEt.text.toString()
        } else {
            searchEt.hint.toString()
        }
    }

    private fun clearText() {
        binding.resultTv.text = ""
    }

    private fun appendText(str: String) {
        binding.resultTv.text = "${binding.resultTv.text}\n$str\n"
    }

    private fun initSearchView() {
        binding.btnSearchResultData.setOnClickListener {
            clearText()

            val localSearchData = getLocalSearchData()
            val localStr = "localSearchData: $localSearchData\n"
            MyLog.i(TAG, localStr)
            binding.resultTv.text = localStr

            val request = SearchRequest(getQuery1(), 20, "all")
            val requestStr = "request: $request\n"
            MyLog.d(TAG, requestStr)
            binding.resultTv.text = "${binding.resultTv.text}\n$requestStr"


            RequestAccessManager.INSTANCE.search(request, object : Callback<SearchResultResponse> {
                override fun onResponse(
                    call: Call<SearchResultResponse>,
                    response: Response<SearchResultResponse>
                ) {
                    val data = response.body()?.aggregations?.data
                    val responseStr = "[response] size: ${data?.size}, data: $data\n"
                    MyLog.d(TAG, responseStr)
                    binding.resultTv.text = "${binding.resultTv.text}\n$responseStr"
                }

                override fun onFailure(call: Call<SearchResultResponse>, t: Throwable) {
                    val responseStr = "[onFailure] t: $t\n"
                    MyLog.e(TAG, responseStr)
                    toast(responseStr)
                    binding.resultTv.text = "${binding.resultTv.text}\n$responseStr\n"
                }
            })
        }

        binding.btnCoroutineSearch.setOnClickListener {
            clearText()

            val localSearchData = getLocalSearchData()
            val localStr = "localSearchData: $localSearchData\n"
            MyLog.i(TAG, localStr)
            binding.resultTv.text = localStr

            val request = SearchRequest(getQuery2(), 2000, "all")
            val requestStr = "request: $request\n"
            MyLog.d(TAG, requestStr)
            binding.resultTv.text = "${binding.resultTv.text}\n$requestStr"

            mainScope.launch {
                val response = withContext(Dispatchers.IO) {
                    RequestAccessManager.INSTANCE.coroutineSearch(request)
                }
                val data = response.aggregations.data
                val responseStr = "[response] size: ${data.size}, data: $data\n"
                MyLog.d(TAG, responseStr)
                binding.resultTv.text = "${binding.resultTv.text}\n$responseStr"
            }
        }

        binding.btnTestData.setOnClickListener {
            Thread {
                testAggregation()
            }.start()


//            val testdata = Gson().fromJson(DataCreator.TEST_DATA, TestData::class.java)
//            MyLog.i(TAG, "testdata: $testdata")
//
//            val data = testdata.data
//            val type: Type = object : TypeToken<Map<String, Person>>() {}.type
//            val map: Map<String, Person> = Gson().fromJson(data, type)
//            MyLog.i(TAG, "map: $map")
//
//            map.forEach { (digit, person) -> MyLog.i(TAG, "$digit: $person") }
        }
    }

    fun testAggregation() {
        val aggregations = Gson().fromJson(
            DataCreator.SEARCH_MEDIA_PATH_LIST,
            SearchResultResponse.Aggregation::class.java
        )
        val dataList: List<MediaPath> = aggregations.data
        MyLog.i(TAG, "dataList: $dataList")
        val mediaItemList = DataConverter.mediaPathList2MediaItemList(dataList)
        MyLog.i(TAG, "mediaItemList: $mediaItemList")
    }


    private fun getLocalSearchData(): List<SearchMediaItem> {
        return DataConverter.mediaPathList2MediaItemList(DataCreator.localSearch().aggregations.data)
    }

    private fun getAggregations(): List<SearchMediaItem> {
        return DataConverter.mediaPathList2MediaItemList(DataCreator.aggregation().data)
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