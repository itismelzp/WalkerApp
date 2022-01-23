package com.walker.apt.annotation;

/**
 * 数据库自动升级注解
 * <p>
 * Created by walkerzpli on 2022/1/23.
 */
public @interface ColumnUpdate {

    int updateVersion() default 1;

    int updateType() default TYPE_ADD;

    int TYPE_ADD    = 0;
    int TYPE_DELETE = 1;
    int TYPE_UPDATE = 2;

}
