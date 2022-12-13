package com.walker.kapt.compiler

import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Elements

/**
 * Created by lizhiping on 2022/12/9.
 * <p>
 * description
 */
open class ElementInfo(private var element: Element, private var elementUtils: Elements?) {

    private var packageName: String? = null // 包名
    private var simpleNameStr: String = "" // 属性名
    private var enclosingElementStr: String = "" // 包名+属性名
    private var kindStr: String = "" // 元素类型（参考：ElementKind）
    private var constantValue: Any? = null // 属性值

    private var enclosedElements: List<*>? = null
    private var modifiers: Set<Modifier> = setOf()

    fun getPackageName(): String? = elementUtils?.getPackageOf(element)?.qualifiedName?.toString()

    fun getSimpleNameStr(): String = element.simpleName.toString()

    fun getEnclosingElementStr(): String = element.enclosingElement.toString()

    fun getKindStr(): String = element.kind.toString()

    fun getConstantValue(): Any? = if (element is VariableElement) {
        (element as VariableElement).constantValue
    } else {
        null
    }

    fun getEnclosedElements(): List<*> = element.enclosedElements
    fun getModifiers(): Set<Modifier> = element.modifiers
}