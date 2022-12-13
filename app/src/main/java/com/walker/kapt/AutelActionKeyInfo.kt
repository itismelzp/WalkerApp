package com.walker.kapt

/**
 * Created by lizhiping on 2022/12/8.
 * <p>
 * description
 */
class AutelActionKeyInfo<D, T>(
    componentVal: Int,
    msgType: String,
    packConvert: IAutelConverter<D, *>,
    unpackConvert: IAutelConverter<*, T>,
) {

    val canGet: Boolean = false
    val canSet: Boolean = false
    val canPerformAction: Boolean = false

    fun canGet(canGet: Boolean): AutelActionKeyInfo<D, T> {
        return this
    }

    fun canSet(canSet: Boolean): AutelActionKeyInfo<D, T> {
        return this
    }

    fun canPerformAction(canPerformAction: Boolean): AutelActionKeyInfo<D, T> {
        return this
    }

}