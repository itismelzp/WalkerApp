package com.tencent.wink.apt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库自动升级注解
 * <p>
 * Created by walkerzpli on 2022/1/23.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface ColumnUpdate {

    int updateVersion() default 1;

    int updateType() default TYPE_ADD;

    int TYPE_ADD    = 0;
    int TYPE_DELETE = 1;
    int TYPE_UPDATE = 2;

}
