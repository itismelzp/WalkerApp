package com.walker.kapt.compiler.generator

import com.google.gson.Gson
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.walker.kapt.compiler.AutelUtil
import com.walker.kapt.compiler.Constant
import java.io.IOException
import javax.annotation.processing.Filer

/**
 * Created by lizhiping on 2022/12/13.
 * <p>
 * description
 */
class ConverterGenerator : IGenerator {

    companion object {

        private val classSet: MutableSet<String> = mutableSetOf()
        private val converterMap: MutableMap<String, ClassName> = mutableMapOf()

        fun putConverter(converter: String) {
            classSet.add(converter)
        }

        fun getClassName(str: String): ClassName? {
            return converterMap[str]
        }

        fun getClassName(clazzName: ClassName): ClassName? {
            return getClassName(clazzName.toString())
        }
    }

    fun generateConverter(filer: Filer, bean: ClassName): ClassName {
        val emptyConverter = ClassName(Constant.PACKAGE_NAME, "AutelEmptyConvert")
        if (converterMap.contains(bean.toString())) {
            return converterMap[bean.toString()] ?: emptyConverter
        }

        val voidBean = ClassName("java.lang", "Void")
        if (bean == voidBean) {
            converterMap[bean.toString()] = emptyConverter
            return emptyConverter
        }

        val formJasonStr = FunSpec.builder("fromJsonStr")
            .addParameter("str", String::class)
            .returns(bean.copy(nullable = true))
            .addStatement(
                "return %T().fromJson(str, %T::class.java)",
                ClassName("com.google.gson", "Gson"),
                bean
            )
            .build()

        val getJsonStr = FunSpec.builder("getJsonStr")
            .returns(String::class.asTypeName().copy(nullable = true))
            .addStatement(
                "return %T().toJson(%T(guid = 123456789))",
//                    ClassName("com.google.gson", "Gson"),
                Gson::class,
                bean
            )
            .build()

        val setValueFromBean = FunSpec.builder("setValueFromBean")
            .addParameter("bean", bean)
            .addStatement("val builder = %T()", bean)
            .addStatement("// builder = converter()")
            .build()

        val getBeanFromMessage = FunSpec.builder("getBeanFromMessage")
            .addModifiers(KModifier.PRIVATE)
            .addParameter("bean", bean)
            .returns(bean.copy(nullable = true))
            .addStatement("val retBean = %T()", bean)
            .addStatement("return retBean")
            .build()

        val clazz = "${bean.simpleName}Converter".replace(".", "")
        val clazzName = ClassName(bean.packageName, clazz)
        val clazzBuilder = TypeSpec.classBuilder(clazzName)
            .addSuperinterface(
                ClassName(
                    Constant.PACKAGE_NAME,
                    "IAutelConverter"
                ).parameterizedBy(bean, bean)
            )
            .addFunction(formJasonStr)
            .addFunction(getJsonStr)
            .addFunction(setValueFromBean)
            .addFunction(getBeanFromMessage)

        val classFile = FileSpec.builder(bean.packageName, clazz)
            .addType(clazzBuilder.build())
            .addComment(Constant.CODE_COMMENT)
            .build()

        try {
            classFile.writeTo(filer)
            converterMap[bean.toString()] = clazzName
            return clazzName
        } catch (e: IOException) {
            println(e.message)
        }
        converterMap[bean.toString()] = emptyConverter
        return emptyConverter
    }

    override fun generate(filer: Filer) {
        classSet.map {
            converterMap.put(it, generateConverter(filer, AutelUtil.parseClassName(it)))
        }
    }
}