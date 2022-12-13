package com.walker.kapt.compiler

import com.squareup.kotlinpoet.ClassName
import com.walker.annotations.AutelKey
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.VariableElement

/**
 * Created by lizhiping on 2022/12/9.
 * <p>
 * description
 */
class AutelUtil {

    companion object {
        fun createElementInfo(element: VariableElement): AutelKeyInfo {
            val annotation = element.getAnnotation(AutelKey::class.java)
            val convertInfo = AutelKeyInfo(
                element = element,
                keyName = annotation.keyName,
                paramBean = getClassNameFromAnnotation(element, "paramBean"),
                resultBean = getClassNameFromAnnotation(element, "resultBean")
            )
            convertInfo.canSet = annotation.canSet
            convertInfo.canGet = annotation.canGet
            convertInfo.canAction = annotation.canAction
            convertInfo.canListen = annotation.canListen
            convertInfo.paramMsg = annotation.paramMsg
            convertInfo.resultMsg = annotation.resultMsg
            return convertInfo
        }

        private fun getClassNameFromAnnotation(key: Element, paramName: String): ClassName {
            return parseClassName(getClassFromAnnotation(key, paramName))
        }

        public fun parseClassName(clazz: String?): ClassName {
            val resultParameterized = ClassName(
                clazz?.let {
                    clazz.substring(
                        0, clazz.lastIndexOf(".")
                    )
                } ?: "java.lang",
                clazz?.let {
                    clazz.substring(
                        clazz.lastIndexOf("."),
                        clazz.length
                    )
                } ?: "Void")
            return resultParameterized
        }

        private fun getClassFromAnnotation(key: Element, paramName: String): String? {
            val annotationMirrors = key.annotationMirrors
            for (annotationMirror in annotationMirrors) {
                if (AutelKey::class.java.name != annotationMirror.annotationType.toString()) {
                    continue
                }
                val keySet: Set<ExecutableElement> = annotationMirror.elementValues.keys
                for (executableElement in keySet) {
                    if (executableElement.simpleName.toString() == paramName) {
                        return annotationMirror.elementValues[executableElement]!!.value.toString()
                    }
                }
            }
            return null
        }
    }
}