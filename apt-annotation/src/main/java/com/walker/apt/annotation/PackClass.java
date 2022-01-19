package com.walker.apt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解生成类对应的编、解码器
 * <p>
 * Created by walkerzpli on 2022/1/14.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface PackClass {
}
