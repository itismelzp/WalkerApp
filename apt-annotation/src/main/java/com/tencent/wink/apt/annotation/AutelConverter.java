package com.tencent.wink.apt.annotation;

/**
 * Created by lizhiping on 2022/10/1.
 * <p>
 * description
 */
public @interface AutelConverter {
    boolean canSet() default false;

    boolean canGet() default false;

    boolean canAction() default false;

    boolean canListen() default false;

    String paramConverter() default "";

    String resultConverter() default "";

    String paramBean() default "";

    String paramMsg() default "";

    String resultBea() default "";

    String resultMsg() default "";
}
