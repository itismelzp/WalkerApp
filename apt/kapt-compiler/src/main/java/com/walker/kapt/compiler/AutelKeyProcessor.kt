package com.walker.kapt.compiler

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.walker.annotations.AutelKey
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.util.Elements

@AutoService(Processor::class)
class AutelKeyProcessor : AbstractProcessor() {

    private var mFiler: Filer? = null
    private var mElementUtils: Elements? = null

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        mFiler = processingEnv?.filer
        mElementUtils = processingEnv?.elementUtils
    }

    // 指定处理的版本
    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    // 给到需要处理的注解
    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val types: LinkedHashSet<String> = LinkedHashSet()
        getSupportedAnnotations().forEach { clazz: Class<out Annotation> ->
            types.add(clazz.canonicalName)
        }
        return types
    }

    private fun getSupportedAnnotations(): Set<Class<out Annotation>> {
        val annotations: LinkedHashSet<Class<out Annotation>> = LinkedHashSet()
        // 需要解析的自定义注解
        annotations.add(AutelKey::class.java)
        return annotations
    }

    override fun process(
        p0: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        // 1）组装map：[ClassName -> List<AutelConvertInfo>]
        val elementInfoMap = assembleAnnotationInfo(roundEnvironment)
        // 2）生成代码
        mFiler?.let {
            CodeGenerator.generateCode(it, elementInfoMap)
            return true
        }
        return false
    }

    private fun assembleAnnotationInfo(roundEnvironment: RoundEnvironment?): LinkedHashMap<ClassName, AutelKeyClassCreatorProxy> {
        val creatorProxyMap = LinkedHashMap<ClassName, AutelKeyClassCreatorProxy>()
        // 有注解就会进来
        roundEnvironment?.getElementsAnnotatedWith(AutelKey::class.java)?.forEach { element ->
            val packageName = mElementUtils?.getPackageOf(element)?.qualifiedName?.toString() ?: ""
            val annotation = element.getAnnotation(AutelKey::class.java)
            val clazz = ClassName(packageName, annotation.keyType)
            var creatorProxy = creatorProxyMap[clazz]
            if (creatorProxy == null) {
                // 2）拿到属性所在的类元素
                creatorProxy = AutelKeyClassCreatorProxy(mElementUtils, element)
                creatorProxyMap[clazz] = creatorProxy
            }
            // 属性注解
            if (element is VariableElement) {
                val elementInfo = AutelUtil.createElementInfo(element)
                creatorProxy.putAutelKeyInfo(annotation, elementInfo)
            }
        }
        return creatorProxyMap
    }

}