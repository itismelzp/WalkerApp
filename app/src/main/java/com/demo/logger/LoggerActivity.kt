package com.demo.logger

import android.media.ExifInterface
import android.media.ExifInterface.TAG_DATETIME
import android.media.ExifInterface.TAG_DATETIME_DIGITIZED
import android.media.ExifInterface.TAG_DATETIME_ORIGINAL
import android.media.ExifInterface.TAG_ORIENTATION
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import com.demo.R
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
import com.demo.utils.ExifUtils
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
import java.io.File
import java.io.InputStreamReader
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
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
        initUploadImages()
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

    private fun multiSlice() {
        clearResultText()
        val keys = DataFactory.getEven(3000)
        val bucketSize = 5
        val total = keys.size
        lifecycleScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                val startTime = System.currentTimeMillis()
                val threadSize = ceil(total / bucketSize.toDouble()).toInt()
                var start: Int
                var end: Int

                var tempResult = ""
                for (i in 0 until threadSize) {
                    start = i * bucketSize
                    end = (i + 1) * bucketSize - 1
                    if (end > total - 1) {
                        end = total - 1
                    }
                    val msg = "slice: ($i/$threadSize), rang: [$start, $end], index: ${
                        keys.slice(
                            start..end
                        )
                    }\n"
                    MyLog.i(TAG, msg)
                    tempResult += msg
                }
                "timeCost: ${System.currentTimeMillis() - startTime}, result: $tempResult"
            }
            appendResultText(result)
        }
    }

    private fun <E> List<E>.groupByIndex(bucketSize: Int): Map<Int, List<E>> {
        val threadSize = ceil(size / bucketSize.toDouble()).toInt()
        var start: Int
        var end: Int
        val map = mutableMapOf<Int, List<E>>()
        for (i in 0 until threadSize) {
            start = i * bucketSize
            end = (i + 1) * bucketSize - 1
            if (end > size - 1) {
                end = size - 1
            }
            val slice = slice(start..end)
            map[i] = slice
        }
        return map
    }

    private fun listSlice() {
        appendResultText()
        val keys = DataFactory.getEven(3000)
        val bucketSize = 5

        val start = System.currentTimeMillis()

        val result: Map<Int, List<Int>> = keys.groupBy {
            keys.indexOf(it) / bucketSize
        }

        appendResultText("timeCost: ${System.currentTimeMillis() - start}, result: $result")
    }

    private fun initUploadImages() {
        binding.btnUploadImages.setOnClickListener {

            val localMediaIds = intArrayOf(
                74103,
                67950,
                70690,
                39714,
                67994,
                60867,
                60145,
                73684,
                43363,
                61694,
                54413,
                73989,
                66482,
                67252,
                67022,
                53678,
                60353,
                57501,
                56547,
                38425,
                71036,
                73688,
                70582,
                66217,
                40655,
                66456,
                54839,
                60032,
                73714,
                68267,
                40555,
                61427,
                69716,
                38848,
                39488,
                68860,
                38745,
                68429,
                40654,
                43371,
                54529,
                67455,
                41981,
                70041,
                61315,
                73531,
                42555,
                52180,
                39913,
                67481,
                38849,
                40556,
                70041,
                43582,
                73684,
                38425,
                39913,
                38745,
                43371,
                54819,
                40556,
                57501,
                68267,
                60778,
                67455,
                61427,
                38849,
                40555,
                70582,
                60145,
                52180,
                42555,
                68429,
                38851,
                39714,
                67022,
                38850,
                39488,
                67252,
                54839,
                60723,
                56547,
                74103,
                37628,
                53882,
                38848,
                73531,
                41981,
                73325,
                71036,
                61694,
                67950,
                60032,
                54413,
                60353,
                37599,
                40654,
                40655,
                68860,
                66217,
                67994,
                60867,
                67481,
                54529,
                70690,
                66456,
                73989,
                53678,
                66482,
                73714,
                61315
            )


            clearResultText()

            val localList = localMediaIds.toList()
            appendResultText("localList size: ${localList.size}, distinct size: ${localList.distinct().size}")


            val localMediaIds2 = intArrayOf(74103,70690,39714,68860,60867,68267,60145,73684,43363,61694,54413,73989,68429,66482,67252,67022,53678,60353,57501,56547,38425,71036,73688,40655,66456,54839,60032,67950,73714,67994,40555,61427,69716,38848,39488,38745,40654,43371,54529,41981,67455,66217,70582,70041,61315,73531,42555,52180,39913,67481,38849,40556)
            appendResultText("localMediaIds2 size: ${localMediaIds2.size}, distinct size: ${localMediaIds2.distinct().size}")

            val local3 = intArrayOf(74103,70690,54413,53678,39714,68860,56547,68267,73684,43363,73989,68429,54529,66482,61315,67252,67022,52180,60867,38425,71036,73688,40655,66456,67950,73714,67994,40555,69716,38848,39488,61694,54839,57501,38745,40654,43371,61427,41981,67455,66217,70582,70041,60145,60353,73531,42555,60032,39913,67481,38849,40556)
            appendResultText("local3 size: ${local3.size}, distinct size: ${local3.distinct().size}")

            val fileList = assets.list("")?.toList()
            fileList?.run {
                MyLog.i(TAG, "[initUploadImages] fileList: $this")
                val photoList: List<String> = fileList.filter { it.endsWith(".jpg", ignoreCase = true) }
                photoList.forEach {
                    val inputStream  = assets.open(it)
                    val exifInterface = ExifInterface(inputStream)
                    val readExif = readExif(exifInterface)
                    val msg = "[initUploadImages] file: $it\n$readExif"
                    MyLog.i(TAG, msg)
                    appendResultText(msg)
                }
            }
            val testImage1 = "/storage/emulated/0/DCIM/4544775236_90726d7289_o.jpg"
            val testImage2 = "/storage/emulated/0/DCIM/2547921893_d86f760e4a_o.jpg"
            val testImage3 = "/storage/emulated/0/DCIM/20662127472_e6c8784ce0_o.jpg"
            val testImage4 = "/storage/emulated/0/DCIM/209842930_65f670d0f0_o.jpg"
            val testImage5 = "/storage/emulated/0/DCIM/2021_04_02_12_11_IMG_1483.JPG"

            val str1 = "testImage1: \n${readExif(testImage1)}"
            appendResultText(str1)
            MyLog.i(TAG, str1)

            val str2 = "testImage2: \n${readExif(testImage2)}"
            appendResultText(str2)
            MyLog.i(TAG, str2)

            val str3 = "testImage3: \n${readExif(testImage3)}"
            appendResultText(str3)
            MyLog.i(TAG, str3)

            val str4 = "testImage4：\n${readExif(testImage4)}"
            appendResultText(str4)
            MyLog.i(TAG, str4)

            val str5 = "testImage5：\n${readExif(testImage5)}"
            appendResultText(str5)
            MyLog.i(TAG, str5)

            initCntText()
            workerViewModel.uploadImages()
//            uploadImagesByThread()
            startJobService()

            val list = mutableListOf<Int>()
            list.add(1)
            list.add(2)
            list.add(3)
            list.add(4)
            list.forEachIndexed { index, i ->
                if (index == 2) {
                    return@forEachIndexed
                }
                appendResultText("[forEachIndexed] i:$i")
            }
        }
    }

    private fun readExif(path: String) : ExifEntry {
        return readExif(ExifInterface(File(path)))
    }

    private fun readExif(exif: ExifInterface): ExifEntry {
        val exifData = ExifEntry()
        exifData.mOrientation = exif.getAttribute(TAG_ORIENTATION)
//        exifData.mDateTime = exif.getAttribute(TAG_DATETIME)
        exifData.mDateTime = exif.getAttribute(TAG_DATETIME)
        exifData.mDateTimeOriginal = exif.getAttribute(TAG_DATETIME_ORIGINAL)
        exifData.mDateTimeDigitized = exif.getAttribute(TAG_DATETIME_DIGITIZED)
        exifData.mTimeStamp = ExifUtils.getDateTime2(exif)
        exifData.timeStamp = timeToDate(exifData.mTimeStamp)
        return exifData
    }

    private fun timeToDate(timeStamp: Long): String {
        val date = Date(timeStamp)
        val sd = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sd.format(date);
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

        binding.btnDataConverter.setOnClickListener {

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

        binding.btnSliceTest.setOnClickListener {
            multiSlice()
            listSlice()
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