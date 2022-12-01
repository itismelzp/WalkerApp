package com.demo.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * Created by lizhiping on 2022/11/30.
 * <p>
 * description
 */
public class Scopes {

 @Suppress("FunctionName")
 public fun NormalScope(): CoroutineScope = CoroutineScope(Dispatchers.Main)

}