package com.walker.kapt.compiler

import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Elements

/**
 * Created by lizhiping on 2022/12/9.
 * <p>
 * description
 */
open class AutelKeyClassCreatorProxy(private val mElementUtils: Elements?, var mElement: Element) {

    val autelInfoMap: MutableMap<Annotation, AutelKeyInfo> = mutableMapOf()
    private var mTypeElement: TypeElement? = null

    init {
        if (mElement is VariableElement) {
            mTypeElement = (mElement.enclosingElement as TypeElement)
        }
    }

    fun getSimpleName(): String = mElement.simpleName.toString()

    fun getPackageName(): String= mElementUtils?.getPackageOf(mTypeElement)?.qualifiedName.toString()

    fun getOriginClassName(): String = mTypeElement?.qualifiedName.toString()

    fun getTargetClassName(): String = mTypeElement?.simpleName.toString() + getSuffix()

    protected open fun getSuffix(): String? {
        return ""
    }

    open fun putAutelKeyInfo(annotation: Annotation, autelKeyInfo: AutelKeyInfo) {
        autelInfoMap[annotation] = autelKeyInfo
    }

}