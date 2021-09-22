package com.walker.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by walkerzpli on 2021/9/22.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BindView {
    int value();
}
