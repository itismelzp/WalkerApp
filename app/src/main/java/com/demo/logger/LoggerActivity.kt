package com.demo.logger

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.demo.R
import com.demo.constant.Constant
import com.demo.databinding.ActivityLoggerLayoutBinding
import com.demo.network.RequestAccessManager
import com.demo.network.model.DataCreator
import com.demo.network.model.FaceScanMetaDataRequest
import com.demo.network.model.MediaFileMetaDataRequest
import com.demo.network.model.MediaPath
import com.demo.network.model.MetaDataResponse
import com.demo.network.model.SearchMediaItem
import com.demo.network.model.SearchRequest
import com.demo.network.model.SearchResultResponse
import com.demo.network.utils.DataConverter
import com.demo.network.utils.GsonUtil
import com.demo.syscomponent.UploadService
import com.demo.utils.DataFactory
import com.demo.utils.DeviceIdUtil
import com.demo.work.UploadMetaDataWorker
import com.demo.work.UploadWorker
import com.demo.work.WorkerViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

class LoggerActivity : BaseActivity<ActivityLoggerLayoutBinding>() {

    private lateinit var logger: MyLog.ILog
    private lateinit var workerViewModel: WorkerViewModel

    companion object {
        private const val TAG = "LoggerActivity"
    }

    override fun initBaseData(savedInstanceState: Bundle?) {
        super.initBaseData(savedInstanceState)
        requestPermission()
        initLog()

        workerViewModel = ViewModelProvider(this)[WorkerViewModel::class.java]

        workerViewModel.uploadImagesWorkInfo.observe(this, uploadImagesObserver())
        workerViewModel.uploadMetaWorkInfo.observe(this, uploadMetaObserver())
    }

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)

        binding.btnDeviceId.text = getString(R.string.device_id, DeviceIdUtil.getDeviceId(this))
        MyLog.i(TAG, "deviceId: ${DeviceIdUtil.getDeviceId(this)}")

        initPingView()
        initMetaUploadView()
        initSearchView()

        binding.btnUploadImages.setOnClickListener {
            initCntText()
            uploadImages()
            uploadImagesByThread()
            startJobService()
        }
        initCntText()
        initSyncView()
    }

    private fun initSyncView() {

        val syncTest = SyncTest()
        syncTest.initHandlerThread()

        binding.btnSyncTest.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {

                val start = System.currentTimeMillis()
                MyLog.i(TAG, "A")
                val local: List<Int> = withContext(Dispatchers.IO) {
                    MyLog.i(TAG, "AA")
                    getLocalData(1_000)
                }
                MyLog.i(TAG, "B")
                val cloud: List<Int> = withContext(Dispatchers.IO) {
                    MyLog.i(TAG, "BB")
                    getCloudData(1_000)
                }
                MyLog.i(TAG, "C")
                appendResultText("timeCost: ${System.currentTimeMillis() - start}ms, all data: ${(local + cloud).sorted()}")
                MyLog.i(TAG, "timeCost: ${System.currentTimeMillis() - start}, all data: ${(local + cloud).sorted()}")
            }

            syncTest.startTest()

        }

        binding.btnSendA.setOnClickListener {
            syncTest.sendMsgA(null)
        }

        binding.btnSendB.setOnClickListener {
            syncTest.sendMsgB(null)
        }

        binding.btnSendC.setOnClickListener {
            syncTest.sendMsgC()
        }
    }

    private fun getLocalData(timeCost: Long): List<Int> {
        TimeUnit.MILLISECONDS.sleep(timeCost)
        return DataFactory.getOdd(10)
    }

    private fun getCloudData(timeCost: Long): List<Int> {
        TimeUnit.MILLISECONDS.sleep(timeCost)
        return DataFactory.getEven(10)
    }

    // 奇数


    private fun multiSlice() {
        lifecycleScope.launch(Dispatchers.IO) {
            val keys = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)
            val bucketSize = 5

            val total = keys.size
            val threadSize = ceil(total / bucketSize.toDouble()).toInt()
            var start: Int
            var end: Int
            for (i in 0 until threadSize) {
                start = i * bucketSize
                end = i * bucketSize + bucketSize - 1
                if (end > total - 1) {
                    end = total - 1
                }

                MyLog.i(
                    TAG,
                    "threadSize: $threadSize, thread: $i, start: $start, end: $end, ${
                        keys.slice(
                            start..end
                        )
                    }"
                )
            }
        }
    }

    private fun initCntText() {
        binding.tvUploadImages.text = resources.getString(R.string.uploadCnt, 0)
        binding.tvUploadMeta.text = resources.getString(R.string.uploadMetaCnt, 0)
    }

    private fun uploadMetaObserver(): Observer<in WorkInfo> {
        return Observer { workInfo ->
            if (workInfo == null) {
                return@Observer
            }
            if (WorkInfo.State.RUNNING == workInfo.state) {
                val progress = workInfo.progress.getInt(UploadMetaDataWorker.PROGRESS, 0)
                binding.tvUploadMeta.text = resources.getString(R.string.uploadCnt, progress)
            }
        }
    }

    private fun uploadImagesObserver(): Observer<in WorkInfo> {
        return Observer { workInfo ->
            if (workInfo == null) {
                return@Observer
            }
            if (WorkInfo.State.RUNNING == workInfo.state) {
                val progress = workInfo.progress.getInt(UploadWorker.PROGRESS, 0)
                binding.tvUploadImages.text = resources.getString(R.string.uploadCnt, progress)
            }
        }
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

            clearResultText()
            val metaData =
                Gson().fromJson(DataCreator.FACE_META_DATA, FaceScanMetaDataRequest::class.java)
            val request = "metaData: ${Gson().toJson(metaData)}"
            MyLog.i(TAG, request)
//            appendResultText(request)
//            toast("metaData: ${Gson().toJson(metaData)}")

            RequestAccessManager.INSTANCE.uploadMetaData(
                metaData,
                object : Callback<MetaDataResponse> {
                    override fun onResponse(
                        call: Call<MetaDataResponse>,
                        response: Response<MetaDataResponse>
                    ) {
                        val responseMsg = "[onResponse] code: ${response.code()}, data: ${response.body()}"
                        MyLog.d(TAG, responseMsg)
//                        toast(msg)
                        appendResultText(responseMsg)

                        val result = response.body()?.result?.let {
                            Gson().fromJson(
                                it.toString(),
                                object : TypeToken<Map<String, Int>>() {}.type
                            ) as Map<String, Int>
                        }

                        appendResultText()
                        appendResultText("result: $result")
                    }

                    override fun onFailure(call: Call<MetaDataResponse>, t: Throwable) {
                        MyLog.d(TAG, "[onFailure] t: $t")
//                        toast("[onFailure] t: $t")
                        appendResultText("[onFailure] t: $t")
                    }
                })

        }
    }

    private fun getQuery(): String {
        val searchEt = binding.searchEt
        return if (!TextUtils.isEmpty(searchEt.text.toString())) {
            searchEt.text.toString()
        } else {
            searchEt.hint.toString()
        }
    }


    private fun clearResultText() {
        binding.resultTv.text = ""
    }

    private fun appendResultText(str: String = "") {
        binding.resultTv.text = "${binding.resultTv.text}\n$str\n"
    }

    private fun initSearchView() {
        binding.btnSearchResultData.setOnClickListener {
            clearResultText()

            val localSearchData = getLocalSearchData()
            val localStr = "localSearchData: $localSearchData\n"
            MyLog.i(TAG, localStr)
            binding.resultTv.text = localStr

            val request = SearchRequest(getQuery(), 2000, "all")
            val requestStr = "request: $request\n"
            MyLog.d(TAG, requestStr)
            appendResultText(requestStr)

            val start = System.currentTimeMillis()
            RequestAccessManager.INSTANCE.search(request, object : Callback<SearchResultResponse> {
                override fun onResponse(
                    call: Call<SearchResultResponse>,
                    response: Response<SearchResultResponse>
                ) {
                    val data = response.body()?.aggregations?.data
                    val end = System.currentTimeMillis()
                    val responseStr = "[response] size: ${data?.size ?: 0}, timeCost: ${end - start}, data: $data\n"
                    MyLog.d(TAG, responseStr)
                    appendResultText(responseStr)
                }

                override fun onFailure(call: Call<SearchResultResponse>, t: Throwable) {
                    val end = System.currentTimeMillis()
                    val responseStr = "[onFailure] timeCost: ${end - start},  t: $t\n"
                    MyLog.e(TAG, responseStr)
                    toast(responseStr)
                    appendResultText(responseStr)
                }
            })
        }

        binding.btnCoroutineSearch.setOnClickListener {
            clearResultText()

            val localSearchData = getLocalSearchData()
            val localStr = "localSearchData: $localSearchData\n"
            MyLog.i(TAG, localStr)
            binding.resultTv.text = localStr

            val request = SearchRequest(getQuery(), 2000, "all")
            val requestStr = "request: $request\n"
            MyLog.d(TAG, requestStr)
            appendResultText(requestStr)

            mainScope.launch {
                val response = withContext(Dispatchers.IO) {
                    RequestAccessManager.INSTANCE.coroutineSearch(request)
                }
                val data = response.aggregations.data
                val responseStr = "[response] size: ${data.size}, data: $data\n"
                MyLog.d(TAG, responseStr)
                appendResultText(requestStr)
            }
        }

        binding.btnCoroutineUserIdSearch.setOnClickListener {
            clearResultText()
            val request = SearchRequest(getQuery(), 2000, "all")
            val start = System.currentTimeMillis()
            RequestAccessManager.INSTANCE.searchWithUserId(
                "1022486851",
                request,
                object : Callback<SearchResultResponse> {
                    override fun onResponse(
                        call: Call<SearchResultResponse>,
                        response: Response<SearchResultResponse>
                    ) {
                        val end = System.currentTimeMillis()
                        val data = response.body()?.aggregations?.data
                        val responseStr =
                            "[response] size: ${data?.size ?: 0},  timeCost: ${end - start}, data: $data\n"
                        MyLog.d(TAG, responseStr)
                        appendResultText(responseStr)
                    }

                    override fun onFailure(call: Call<SearchResultResponse>, t: Throwable) {
                        val end = System.currentTimeMillis()
                        val responseStr = "[onFailure] timeCost: ${end - start}, t: $t\n"
                        MyLog.e(TAG, responseStr)
                        toast(responseStr)
                        appendResultText(responseStr)
                    }
                }
            )
        }

        binding.btnTestData.setOnClickListener {

            mainScope.launch {
                testAggregation()
            }

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

    private suspend fun testAggregation() {
        clearResultText()
        val aggregation = withContext(Dispatchers.IO) {
            getAggregation()
        }
        val dataList: List<MediaPath> = aggregation.data
        MyLog.i(TAG, "dataList: $dataList")
        appendResultText("dataList: $dataList")

        val mediaItemList = withContext(Dispatchers.IO) {
            DataConverter.mediaPathList2MediaItemList(aggregation.data)
        }
        MyLog.i(TAG, "mediaItemList: $mediaItemList")
        appendResultText("mediaItemList: $mediaItemList")
    }

    private fun getAggregation(): SearchResultResponse.Aggregation {
        return Gson().fromJson(
            DataCreator.SEARCH_MEDIA_PATH_LIST,
            SearchResultResponse.Aggregation::class.java
        )
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
            appendLine(output, "exec cmd success:$cmd")
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

    private fun uploadImages() {
        val uploadWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<UploadWorker>()
                .addTag(UploadWorker.TAG)
                .setId(UploadWorker.ID)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .setRequiresCharging(true)
                        .build()
                )
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL, 10_000L,
                    TimeUnit.MILLISECONDS
                )
                .build()
        val uploadMetaDataWorker: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<UploadMetaDataWorker>()
                .addTag(UploadMetaDataWorker.TAG)
                .setId(UploadMetaDataWorker.ID)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .build()
                )
                .build()

        WorkManager.getInstance(this)
            .beginUniqueWork(
                Constant.UPLOAD_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                uploadWorkRequest
            )
            .then(uploadMetaDataWorker)
            .enqueue()


    }

    private fun uploadImagesByThread() {
        Thread {
            for (i in 1..1000) {
                MyLog.i(TAG, "[uploadImagesByThread]: $i")
                TimeUnit.MILLISECONDS.sleep(500L)
            }
        }.start()
    }

    private fun startJobService() {
        UploadService.scheduleJob(this)
    }
}