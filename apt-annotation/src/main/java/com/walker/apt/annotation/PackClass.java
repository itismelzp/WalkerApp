package com.walker.apt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by walkerzpli on 2022/1/14.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface PackClass {
}
