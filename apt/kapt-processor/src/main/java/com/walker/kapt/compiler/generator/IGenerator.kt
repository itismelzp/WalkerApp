package com.walker.kapt.compiler.generator

import javax.annotation.processing.Filer

/**
 * Created by lizhiping on 2022/12/13.
 * <p>
 * description
 */
interface IGenerator {
    fun generate(filer: Filer)
}