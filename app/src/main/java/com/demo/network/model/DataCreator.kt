package com.demo.network.model

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */
object DataCreator {

    const val TEST_DATA = "{\n" +
            "    \"city\":\"沈阳\",\n" +
            "    \"data\":{\n" +
            "        \"1\":{\n" +
            "            \"name\":\"hello\"\n" +
            "        },\n" +
            "        \"2\":{\n" +
            "            \"name\":\"hello2\"\n" +
            "        }\n" +
            "    }\n" +
            "}"

    const val SEARCH_RESULT_DATA = "{\n" +
            "    \"num_hits\": 22,\n" +
            "    \"hits\": [],\n" +
            "    \"elapsed_time_micros\": 0,\n" +
            "    \"errors\": [],\n" +
            "    \"aggregations\": {\n" +
            "        \"data\": [\n" +
            "            {\n" +
            "                \"day\": 23,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 1,\n" +
            "                \"name\": \"2021_01_23_16_21_IMG_0533.jpg\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2021_01_23_16_21_IMG_0533.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=5084cc96fb15d0dc9f2532558df7463d20ef5d847dc82ac1c11b5323031d03d1\",\n" +
            "                \"timestamp\": 1611390112,\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2021_01_23_16_21_IMG_0533.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=367c665382f3dbb611d9121fe97e9eda74c380495e12f3f88cdb2164091c7016\",\n" +
            "                \"year\": 2021\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 23,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 1,\n" +
            "                \"name\": \"2021_01_23_10_55_IMG_0514.jpg\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2021_01_23_10_55_IMG_0514.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=dbcaee24837132880d9048f5fcd4625633b61eebe8af27b3d34ecfbaaba6ef48\",\n" +
            "                \"timestamp\": 1611370557,\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2021_01_23_10_55_IMG_0514.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=57514c651ec805aa4196a80ce80f26535a289d319e78b19161d597e1f6006dac\",\n" +
            "                \"year\": 2021\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 23,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 1,\n" +
            "                \"name\": \"IMG_20230123_132316.jpg\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/IMG_20230123_132316.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=f7aaab21e9035b2a96a79f5094878e4369e9a10fab341b276bb4bdc9d216ca4e\",\n" +
            "                \"timestamp\": 1674451397,\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/IMG_20230123_132316.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=bb8ce0aee83aaf86d3589e2e100c74a0b05e5d0f87b5f8b87e78ad9a1f160031\",\n" +
            "                \"year\": 2023\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 23,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 1,\n" +
            "                \"name\": \"2021_01_23_16_04_IMG_0531.jpg\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2021_01_23_16_04_IMG_0531.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=778e681fd1e870639e8f4035776b4da0e5e68a276187c0849a9266043963c7f6\",\n" +
            "                \"timestamp\": 1611389093,\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2021_01_23_16_04_IMG_0531.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=a600a12ef932b3faea7cf7a2cb6f511f6095f88a35843af3373affb7147cc640\",\n" +
            "                \"year\": 2021\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 23,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 1,\n" +
            "                \"name\": \"IMG_20230123_132310.jpg\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/IMG_20230123_132310.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=482b76163c57e0092326bad298393fc44324ccf866c2322961ad952afce328bf\",\n" +
            "                \"timestamp\": 1674451392,\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/IMG_20230123_132310.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=cef03df55b770b2147d0e81a0ea6a6f6e149db2cc97fe4a96200d33fa12cf710\",\n" +
            "                \"year\": 2023\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 23,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 1,\n" +
            "                \"name\": \"2021_01_23_10_57_IMG_0517.jpg\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2021_01_23_10_57_IMG_0517.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=70bdf96d80876c3980786cba977be6fa4698ff66d58014d0831ce5b41d2d8d3f\",\n" +
            "                \"timestamp\": 1611370648,\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2021_01_23_10_57_IMG_0517.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=3aa5df81f8584080705688c1dabcd98b2f7a44ee3e7e5458135cc65193a676f5\",\n" +
            "                \"year\": 2021\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 23,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 1,\n" +
            "                \"name\": \"2021_01_23_16_21_IMG_0532.jpg\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2021_01_23_16_21_IMG_0532.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=50c06600b578ae841ea108895f425f179eb724863671726073eb8799e5a83c12\",\n" +
            "                \"timestamp\": 1611390109,\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2021_01_23_16_21_IMG_0532.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=3493f6c7b655567e614aed9fc1117d54684f0aedfea88c36ffdd07c255f12708\",\n" +
            "                \"year\": 2021\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 23,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 1,\n" +
            "                \"name\": \"2021_01_23_10_42_IMG_0508.jpg\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2021_01_23_10_42_IMG_0508.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=7df20bf047694ebf90b7009218cef4ffc93c32da42846bdc6d70cf9fc174aeb2\",\n" +
            "                \"timestamp\": 1611369767,\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2021_01_23_10_42_IMG_0508.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=b59f6b10a55936e94594135a9976b7fe59720db6f3e5cd922b13013095a7b0bc\",\n" +
            "                \"year\": 2021\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 23,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 1,\n" +
            "                \"name\": \"2021_01_23_10_55_IMG_0513.jpg\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2021_01_23_10_55_IMG_0513.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=3bfa5646076db66fca8cb7822e7715cdd38a1be9e8c97830adedff5071071f5b\",\n" +
            "                \"timestamp\": 1611370556,\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2021_01_23_10_55_IMG_0513.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=eb19c8728bf4742fc744d3f6fb964e6c06fe825595feb8f1b60598dbbd46e300\",\n" +
            "                \"year\": 2021\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 23,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 1,\n" +
            "                \"name\": \"2021_01_23_10_42_IMG_0506.jpg\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2021_01_23_10_42_IMG_0506.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=de7d61e77b5eddb20eede9edfe137feb9c4e51ec0cdf88846f0ff4daee98a7c4\",\n" +
            "                \"timestamp\": 1611369731,\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2021_01_23_10_42_IMG_0506.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=00db3a4774cbf337ceadb889518bd1108b57e618479a7782e7d43593cead40f8\",\n" +
            "                \"year\": 2021\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 23,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 1,\n" +
            "                \"name\": \"2021_01_23_10_45_IMG_0519.JPG\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2021_01_23_10_45_IMG_0519.JPG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=df35e7c235f2c63f126b399667760a2d37cd25aae2b18a4fbc772386b1132b83\",\n" +
            "                \"timestamp\": 1611369906,\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2021_01_23_10_45_IMG_0519.JPG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=1b0d683625ab736c2d055d79f776bf9047914208882f2b2a6a9b7bbc00ff6a58\",\n" +
            "                \"year\": 2021\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 1,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 1,\n" +
            "                \"name\": \"eaad9fe23d04aee4cee227a92bcf156d.jpg\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/eaad9fe23d04aee4cee227a92bcf156d.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=ae0cf366404a15e57a62d8dcfbeaff95a90769451d3fcf669bf9dff733e530d7\",\n" +
            "                \"timestamp\": 94665600,\n" +
            "                \"type_\": [\n" +
            "                    1\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/eaad9fe23d04aee4cee227a92bcf156d.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=269dac4490985c2ce30b253678ef689deb6c959d63afe19ed3a664533a05e548\",\n" +
            "                \"year\": 1973\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 1,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 4,\n" +
            "                \"name\": \"2022_04_01_23_57_IMG_9521.JPG\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2022_04_01_23_57_IMG_9521.JPG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065939Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=553cc3c60012f17bc05d889c52d2891be006308d66eaa6e53f9d0381e09f4010\",\n" +
            "                \"timestamp\": 1648828624,\n" +
            "                \"type_\": [\n" +
            "                    2\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2022_04_01_23_57_IMG_9521.JPG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065939Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=78d074c5ca7b897dae89157040a0198ee69aa2ffea56df14aac88d473574a2e2\",\n" +
            "                \"year\": 2022\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 28,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 10,\n" +
            "                \"name\": \"2021_10_28_19_16_IMG_6343.JPG\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2021_10_28_19_16_IMG_6343.JPG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065939Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=5421478744909c72a06bb439cb2a3fca6dc1bd762fca88aaef9a6ae24d1bf3af\",\n" +
            "                \"timestamp\": 1635419771,\n" +
            "                \"type_\": [\n" +
            "                    2\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2021_10_28_19_16_IMG_6343.JPG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065939Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=f1bab5b93bf7c86ebd4833624af8a6bf52fb20a073de4ba94df788c6f44e1677\",\n" +
            "                \"year\": 2021\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 1,\n" +
            "                \"latitude\": 22.689677777777774,\n" +
            "                \"longitude\": 114.75266388888888,\n" +
            "                \"month\": 11,\n" +
            "                \"name\": \"2021_11_01_20_43_IMG_6508.jpg\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2021_11_01_20_43_IMG_6508.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=d1ea77bdb09c96d4829a1ee7decdb1d052520aeeba84eaabfaae0551dda9b802\",\n" +
            "                \"timestamp\": 1635770635,\n" +
            "                \"type_\": [\n" +
            "                    2\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2021_11_01_20_43_IMG_6508.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=12a797140b987239b1a7ac3d5815c982084527b86a559de54a22a1cd3fdd3c75\",\n" +
            "                \"year\": 2021\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 2,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 4,\n" +
            "                \"name\": \"2022_04_02_23_01_IMG_9579.JPG\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2022_04_02_23_01_IMG_9579.JPG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=75c61cc75c03006fe59d630c04e8281be382d82614991b6e35048fbe1f3a32ec\",\n" +
            "                \"timestamp\": 1648911703,\n" +
            "                \"type_\": [\n" +
            "                    2\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2022_04_02_23_01_IMG_9579.JPG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=22f9d88066562a7c933f7875850508787e8f9e767a45943303ad0b546c61679e\",\n" +
            "                \"year\": 2022\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 30,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 11,\n" +
            "                \"name\": \"2021_11_30_17_08_IMG_7243.jpg\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2021_11_30_17_08_IMG_7243.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=ea809146b6428ab66b881b1f33cdb43df8060ca4feac62fed53a004378676d80\",\n" +
            "                \"timestamp\": 1638263308,\n" +
            "                \"type_\": [\n" +
            "                    2\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2021_11_30_17_08_IMG_7243.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=d73310a6898625c50506900ae04a4c6f7b3073b1c3943d1b6910f881ad7449c2\",\n" +
            "                \"year\": 2021\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 23,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 7,\n" +
            "                \"name\": \"2022_07_23_11_33_IMG_2920.jpg\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2022_07_23_11_33_IMG_2920.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=4778fd1af22773b545b25f4829bad109823cc017803e4b207f23d31184e0d609\",\n" +
            "                \"timestamp\": 1658547216,\n" +
            "                \"type_\": [\n" +
            "                    2\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2022_07_23_11_33_IMG_2920.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=481a6ac393d581b55728bbfe926fab44a5cf1ff39f7f326042142a54880bb6e0\",\n" +
            "                \"year\": 2022\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 18,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 10,\n" +
            "                \"name\": \"2022_10_18_19_31_IMG_4368.PNG\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2022_10_18_19_31_IMG_4368.PNG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=a318217fb5b21287f580b7c67e41c5c13fdb91d03c6339ea6ac852abc42996d3\",\n" +
            "                \"timestamp\": 1666092696,\n" +
            "                \"type_\": [\n" +
            "                    2\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2022_10_18_19_31_IMG_4368.PNG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=88baa362d08442ca42197dc027666e225e46cba2f84d5e123d74afaa21d4423b\",\n" +
            "                \"year\": 2022\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 9,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 9,\n" +
            "                \"name\": \"2021_09_23_17_33_IMG_5508.JPG\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2021_09_23_17_33_IMG_5508.JPG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=0dfdbb03cb100340a4dbd27012b50b3dabbd7ba460cf3bc0c84dbb1e476ce802\",\n" +
            "                \"timestamp\": 1631175737,\n" +
            "                \"type_\": [\n" +
            "                    2\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2021_09_23_17_33_IMG_5508.JPG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=a5ea7e22f8ee15df15a145e814e1aec9828afaa4b8abca0f9a15fadcca64748d\",\n" +
            "                \"year\": 2021\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 9,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 9,\n" +
            "                \"name\": \"2021_09_23_17_34_IMG_5517.JPG\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2021_09_23_17_34_IMG_5517.JPG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=3444c79ce950b9889c2d28340a560e43c5648f89ae5797f558e0c2dfb1361402\",\n" +
            "                \"timestamp\": 1631177474,\n" +
            "                \"type_\": [\n" +
            "                    2\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2021_09_23_17_34_IMG_5517.JPG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=33961d5d5ee25278ff781980a82a4c4ca22a4b0158e3db4db6095babd38787c9\",\n" +
            "                \"year\": 2021\n" +
            "            },\n" +
            "            {\n" +
            "                \"day\": 2,\n" +
            "                \"latitude\": -1,\n" +
            "                \"longitude\": -1,\n" +
            "                \"month\": 4,\n" +
            "                \"name\": \"2022_04_02_19_22_IMG_9556.JPG\",\n" +
            "                \"thumb_url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/thumb/2022_04_02_19_22_IMG_9556.JPG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=49a972e7c9a9d2422d64740dc7cc0b830873840b429d2f957d7a12a7d640a76c\",\n" +
            "                \"timestamp\": 1648898521,\n" +
            "                \"type_\": [\n" +
            "                    2\n" +
            "                ],\n" +
            "                \"url\": \"https://cubefs-norths3-cn.heytapmobi.com/search-hfs/src/2022_04_02_19_22_IMG_9556.JPG?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=17Rvm12m7RGl913W%2F20230228%2Fsearch-hfs%2Fs3%2Faws4_request&X-Amz-Date=20230228T065940Z&X-Amz-Expires=7776000&X-Amz-SignedHeaders=host&X-Amz-Signature=940ab7fac480c30a19c56fa863f486c2b3032fe456b552932c89ced1b369ecf5\",\n" +
            "                \"year\": 2022\n" +
            "            }\n" +
            "        ],\n" +
            "        \"meta\": [\n" +
            "            \"default\",\n" +
            "            \"ocr\",\n" +
            "            \"vec\"\n" +
            "        ]\n" +
            "    }\n" +
            "}"

    const val MEDIA_META_DATA = "{\n" +
            "    \"userId\": \"B3A0720A6028980A581B46959510BBF09ACC945F\",\n" +
            "    \"mediaFileMetaDatas\": [\n" +
            "        {\n" +
            "            \"createTime\": 1677848084,\n" +
            "            \"fileId\": \"https://ocs-cn-north1.heytapcs.com/album//beta/data6152bc0ce6e5d805\",\n" +
            "            \"fileMD5\": \"xxxxx\",\n" +
            "            \"fileMediaType\": 1,\n" +
            "            \"filePath\": \"local_path/image1\",\n" +
            "            \"mediaId\": 1234,\n" +
            "            \"latitude\": 22.64349746704102,\n" +
            "            \"longitude\": 113.8234405517578,\n" +
            "            \"width\": 3072,\n" +
            "            \"height\": 4096\n" +
            "        },\n" +
            "        {\n" +
            "            \"createTime\": 1676180325351,\n" +
            "            \"fileId\": \"https://ocs-cn-north1.heytapcs.com/album//beta/data6152bc0ce6e5d805\",\n" +
            "            \"fileMD5\": \"xxxxx\",\n" +
            "            \"fileMediaType\": 1,\n" +
            "            \"filePath\": \"local_path/image2\",\n" +
            "            \"mediaId\": 1450,\n" +
            "            \"latitude\": 22.64349746704102,\n" +
            "            \"longitude\": 113.8234405517578,\n" +
            "            \"width\": 3072,\n" +
            "            \"height\": 4096\n" +
            "        }\n" +
            "    ]\n" +
            "}"

    const val FACE_META_DATA = "{\n" +
            "    \"userId\": \"B3A0720A6028980A581B46959510BBF09ACC945F\",\n" +
            "    \"faceScanMetaDatas\": [\n" +
            "        {\n" +
            "            \"mediaId\": 1451,\n" +
            "            \"fileId\": \"ocs_path/image1\",\n" +
            "            \"groupId\": 4,\n" +
            "            \"groupName\": \"小明\",\n" +
            "            \"groupRelation\": 11,\n" +
            "            \"relationDesc\": \"朋友\",\n" +
            "            \"rectLeft\": 584,\n" +
            "            \"rectTop\": 1093,\n" +
            "            \"rectRight\": 761,\n" +
            "            \"rectBottom\": 1262,\n" +
            "            \"mediaType\": 1,\n" +
            "            \"thumbWidth\": 960,\n" +
            "            \"thumbHeight\": 720\n" +
            "        },\n" +
            "        {\n" +
            "            \"mediaId\": 1451,\n" +
            "            \"fileId\": \"ocs_path/image1\",\n" +
            "            \"groupId\": 4,\n" +
            "            \"groupName\": \"小明\",\n" +
            "            \"groupRelation\": 11,\n" +
            "            \"relationDesc\": \"朋友\",\n" +
            "            \"rectLeft\": 584,\n" +
            "            \"rectTop\": 1093,\n" +
            "            \"rectRight\": 761,\n" +
            "            \"rectBottom\": 1262,\n" +
            "            \"mediaType\": 1,\n" +
            "            \"thumbWidth\": 960,\n" +
            "            \"thumbHeight\": 720\n" +
            "        }\n" +
            "    ]\n" +
            "}\n"
}