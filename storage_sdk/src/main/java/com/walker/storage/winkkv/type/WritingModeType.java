package com.walker.storage.winkkv.type;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 文件写入模式
 * <p>
 * Created by walkerzpli on 2022/1/9.
 */

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@IntDef({
        WritingModeType.NON_BLOCKING,
        WritingModeType.ASYNC_BLOCKING,
        WritingModeType.SYNC_BLOCKING
})
@Retention(RetentionPolicy.SOURCE)
public @interface WritingModeType {
    int NON_BLOCKING    = 0;
    int ASYNC_BLOCKING  = 1;
    int SYNC_BLOCKING   = 2;
}
