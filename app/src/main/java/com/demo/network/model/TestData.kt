package com.demo.network.model

import com.google.gson.JsonObject

/**
 * Created by lizhiping on 2023/3/8.
 * <p>
 * description
 */
data class TestData(
    var city: String,
    var data: JsonObject
)

data class Person(
    var name: String
)
