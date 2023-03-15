package com.demo.network.model

import com.google.gson.Gson

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
    
    
    const val SEARCH_RESULT_DEMO_DATA = "{\n" +
            "    \"meta\": [\n" +
            "        \"时间\",\n" +
            "        \"图集\",\n" +
            "        \"文字识别\",\n" +
            "        \"人物\",\n" +
            "        \"地点\",\n" +
            "        \"智能场景\",\n" +
            "        \"回忆\"\n" +
            "    ],\n" +
            "    \"data\": [{\n" +
            "        \"mediaPath\": \"path1\",\n" +
            "        \"type_\": [1, 2], // 对应meta数据 (type 可能是关键字)\n" +
            "        \"name\": [\n" +
            "            [\"深圳\"],\n" +
            "            [\"深圳\", \"深圳市\"] // 目前端侧搜索结果中，同一类型的标签可能会有多个结果，\n" +
            "                             // 比如\"地点\"标签\n" +
            "        ],\n" +
            "        \"groupId\": \"1\", //groupId 为人物聚类分组Id\n" +
            "        \"mediaId\": 1234,\n" +
            "        \"dataTaken\": 1677848084\n" +
            "    }, {\n" +
            "        \"mediaPath\": \"path2\",\n" +
            "        \"type_\": [0, 4],\n" +
            "        \"name\": [\n" +
            "            [\"深圳\"],\n" +
            "            [\"深圳\", \"深圳市\"]\n" +
            "        ],\n" +
            "        \"groupId\": \"2\",\n" +
            "        \"mediaId\": 1234,\n" +
            "        \"dataTaken\": 1677848084\n" +
            "    }]\n" +
            "}\n"


    const val SEARCH_MEDIA_PATH_LIST = "{\n" +
            "    \"meta\": [\n" +
            "        \"时间\",\n" +
            "        \"图集\",\n" +
            "        \"文字识别\",\n" +
            "        \"人物\",\n" +
            "        \"地点\",\n" +
            "        \"智能场景\",\n" +
            "        \"回忆\",\n" +
            "        \"放大镜\"\n" +
            "    ],\n" +
            "    \"data\": [{\n" +
            "        \"mediaId\": 1234,\n" +
            "        \"dataToken\": 1677848084,\n" +
            "        \"type_\": [1, 4, 5],\n" +
            "        \"name\": [\n" +
            "            [\"植物图集\"],\n" +
            "            [\"植物园\", \"花卉市场\"],\n" +
            "            [\"植物\", \"植物识别\"]\n" +
            "        ],\n" +
            "        \"subName\": [\n" +
            "            [\"花\", \"草\", \"树木\"],\n" +
            "            [\"梅花\", \"仙人掌\"]\n" +
            "        ]\n" +
            "    }, {\n" +
            "        \"mediaId\": 1235,\n" +
            "        \"dataToken\": 1677848084,\n" +
            "        \"type_\": [0, 2, 5],\n" +
            "        \"name\": [\n" +
            "            [\"一月\"],\n" +
            "            [\"植物文案\", \"植物文本\"],\n" +
            "            [\"植物\"]\n" +
            "        ],\n" +
            "        \"subName\": [\n" +
            "            [\"花\", \"苔藓\"]\n" +
            "        ]\n" +
            "    }]\n" +
            "}"

    const val SEARCH_SIMPLE_RESULT_DATA = "{\n" +
            "    \"num_hits\": 5,\n" +
            "    \"hits\": [],\n" +
            "    \"elapsed_time_micros\": 0,\n" +
            "    \"errors\": [],\n" +
            "    \"aggregations\": {\n" +
            "        \"data\": [\n" +
            "            {\n" +
            "                \"dataToken\": 1483134755,\n" +
            "                \"mediaId\": 1,\n" +
            "                \"mediaPath\": \"6026\",\n" +
            "                \"name\": [\n" +
            "                    [\n" +
            "                        \"123\"\n" +
            "                    ]\n" +
            "                ],\n" +
            "                \"subName\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1453490613,\n" +
            "                \"mediaId\": 2,\n" +
            "                \"mediaPath\": \"4064\",\n" +
            "                \"name\": [\n" +
            "                    [\n" +
            "                        \"123\"\n" +
            "                    ]\n" +
            "                ],\n" +
            "                \"subName\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1483144754,\n" +
            "                \"mediaId\": 3,\n" +
            "                \"mediaPath\": \"15881\",\n" +
            "                \"name\": [\n" +
            "                    [\n" +
            "                        \"123\"\n" +
            "                    ]\n" +
            "                ],\n" +
            "                \"subName\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1483134579,\n" +
            "                \"mediaId\": 4,\n" +
            "                \"mediaPath\": \"5485\",\n" +
            "                \"name\": [\n" +
            "                    [\n" +
            "                        \"123\"\n" +
            "                    ]\n" +
            "                ],\n" +
            "                \"subName\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1169526568,\n" +
            "                \"mediaId\": 5,\n" +
            "                \"mediaPath\": \"8568\",\n" +
            "                \"name\": [\n" +
            "                    [\n" +
            "                        \"123\"\n" +
            "                    ]\n" +
            "                ],\n" +
            "                \"subName\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            }\n" +
            "        ],\n" +
            "        \"meta\": [\n" +
            "            \"时间\",\n" +
            "            \"文字识别\"\n" +
            "        ]\n" +
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
            "                \"dataToken\": 1660376553,\n" +
            "                \"mediaId\": 1,\n" +
            "                \"mediaPath\": \"2022_08_13_15_42_IMG_3247.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1655198036,\n" +
            "                \"mediaId\": 2,\n" +
            "                \"mediaPath\": \"2022_06_14_17_13_IMG_0963.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1620397766,\n" +
            "                \"mediaId\": 3,\n" +
            "                \"mediaPath\": \"2021_05_07_22_29_IMG_2210.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1629296113,\n" +
            "                \"mediaId\": 4,\n" +
            "                \"mediaPath\": \"2021_08_18_22_15_IMG_4752.JPG\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1655289615,\n" +
            "                \"mediaId\": 5,\n" +
            "                \"mediaPath\": \"2022_06_15_18_40_IMG_1183.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1634722807,\n" +
            "                \"mediaId\": 6,\n" +
            "                \"mediaPath\": \"2021_10_20_17_40_IMG_6031.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1657710627,\n" +
            "                \"mediaId\": 7,\n" +
            "                \"mediaPath\": \"2022_07_13_19_10_IMG_2168.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1628517568,\n" +
            "                \"mediaId\": 8,\n" +
            "                \"mediaPath\": \"2021_08_09_21_59_IMG_4566.JPG\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1630761024,\n" +
            "                \"mediaId\": 9,\n" +
            "                \"mediaPath\": \"2021_09_04_21_10_IMG_5099.JPG\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1655444145,\n" +
            "                \"mediaId\": 10,\n" +
            "                \"mediaPath\": \"2022_06_17_13_35_IMG_1638.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    0\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1611390112,\n" +
            "                \"mediaId\": 1,\n" +
            "                \"mediaPath\": \"2021_01_23_16_21_IMG_0533.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    1\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1611370557,\n" +
            "                \"mediaId\": 2,\n" +
            "                \"mediaPath\": \"2021_01_23_10_55_IMG_0514.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    1\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1674451397,\n" +
            "                \"mediaId\": 3,\n" +
            "                \"mediaPath\": \"IMG_20230123_132316.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    1\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1611389093,\n" +
            "                \"mediaId\": 4,\n" +
            "                \"mediaPath\": \"2021_01_23_16_04_IMG_0531.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    1\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1674451392,\n" +
            "                \"mediaId\": 5,\n" +
            "                \"mediaPath\": \"IMG_20230123_132310.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    1\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1611370648,\n" +
            "                \"mediaId\": 6,\n" +
            "                \"mediaPath\": \"2021_01_23_10_57_IMG_0517.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    1\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1611390109,\n" +
            "                \"mediaId\": 7,\n" +
            "                \"mediaPath\": \"2021_01_23_16_21_IMG_0532.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    1\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1611369767,\n" +
            "                \"mediaId\": 8,\n" +
            "                \"mediaPath\": \"2021_01_23_10_42_IMG_0508.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    1\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1611370556,\n" +
            "                \"mediaId\": 9,\n" +
            "                \"mediaPath\": \"2021_01_23_10_55_IMG_0513.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    1\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1611369731,\n" +
            "                \"mediaId\": 10,\n" +
            "                \"mediaPath\": \"2021_01_23_10_42_IMG_0506.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    1\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 1611369906,\n" +
            "                \"mediaId\": 11,\n" +
            "                \"mediaPath\": \"2021_01_23_10_45_IMG_0519.JPG\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    1\n" +
            "                ]\n" +
            "            },\n" +
            "            {\n" +
            "                \"dataToken\": 94665600,\n" +
            "                \"mediaId\": 1,\n" +
            "                \"mediaPath\": \"eaad9fe23d04aee4cee227a92bcf156d.jpg\",\n" +
            "                \"name\": [],\n" +
            "                \"type_\": [\n" +
            "                    2\n" +
            "                ]\n" +
            "            }\n" +
            "        ],\n" +
            "        \"meta\": [\n" +
            "            \"vec\",\n" +
            "            \"default\",\n" +
            "            \"ocr\"\n" +
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

    fun localSearch(): SearchResultResponse {
        return Gson().fromJson(SEARCH_SIMPLE_RESULT_DATA, SearchResultResponse::class.java)
    }

    fun aggregation(): SearchResultResponse.Aggregation {
        return Gson().fromJson(SEARCH_MEDIA_PATH_LIST, SearchResultResponse.Aggregation::class.java)
    }
}