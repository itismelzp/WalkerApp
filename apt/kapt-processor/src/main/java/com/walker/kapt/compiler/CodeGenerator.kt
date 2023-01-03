package com.walker.kapt.compiler

import com.google.gson.Gson
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.walker.kapt.compiler.generator.ConverterGenerator
import java.io.IOException
import javax.annotation.processing.Filer
import javax.lang.model.element.TypeElement

/**
 * Created by lizhiping on 2022/12/9.
 * <p>
 * description
 */
class CodeGenerator {

    companion object {

        private val converterMap: MutableMap<String, ClassName> = mutableMapOf()

        fun generateCode(
            filer: Filer,
            elementInfoMap: LinkedHashMap<ClassName, AutelKeyClassCreatorProxy>
        ) {
            elementInfoMap.entries.forEach {
                generateCode(filer, it.key, it.value)
            }
        }

        private fun generateCode(
            filer: Filer,
            clazz: ClassName,
            creatorProxy: AutelKeyClassCreatorProxy
        ) {
            // component属性
            val componentClass = ClassName(clazz.packageName, "ComponentType")
            // 构造类
            val clazzBuilder = TypeSpec.objectBuilder(clazz.simpleName)
            val componentProperty = PropertySpec
                .builder("component", componentClass)
                .initializer("ComponentType.MISSION")
                .build()
            clazzBuilder.addProperty(componentProperty)
            creatorProxy.autelInfoMap.forEach {
                val info = it.value
                println("forEach it: $it")
                // autelKey属性
                val autelKeyInfoClass = ClassName(
                    Constant.PACKAGE_NAME,
                    "AutelActionKeyInfo"
                )

                val parameterizedAutelKeyInfoClass =
                    autelKeyInfoClass.parameterizedBy(info.paramBean, info.resultBean)

                val paramConverter = generateConverter(filer, info.paramBean)
                val resultConverter = generateConverter(filer, info.resultBean)
                val initCode = "%T(\n" +
                        "    component.value,\n" +
                        "    %M,\n" +
                        "    %T(),\n" +
                        "    %T()\n" +
                        ").canGet(%L).canSet(%L).canPerformAction(%L)"
                val msgType = MemberName(
                    (info.element.enclosingElement as TypeElement).qualifiedName.toString(),
                    info.element.simpleName.toString()
                )
                // 类属性
                val keyProperty = PropertySpec.builder(info.keyName, parameterizedAutelKeyInfoClass)
                    .initializer(
                        initCode,
                        parameterizedAutelKeyInfoClass,
                        msgType,
                        paramConverter,
                        resultConverter,
                        info.canGet,
                        info.canSet,
                        info.canAction
                    )
                clazzBuilder.addProperty(keyProperty.build())
            }

            // 生成类文件
            val classFile = FileSpec.builder(clazz.packageName, clazz.simpleName)
                .addType(clazzBuilder.build())
                .addComment(Constant.CODE_COMMENT)
                .build()

            println("classFile------------>\n$classFile")
            try {
                classFile.writeTo(filer)
            } catch (e: IOException) {
                println(e.message)
            }
        }

        private fun generateConverter(filer: Filer, bean: ClassName): ClassName {
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
    }

}