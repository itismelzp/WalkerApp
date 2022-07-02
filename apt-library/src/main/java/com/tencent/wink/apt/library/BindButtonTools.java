package com.tencent.wink.apt.library;

import android.app.Activity;
import android.util.Log;

import com.tencent.wink.apt.utils.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by walkerzpli on 2021/10/09.
 */
public class BindButtonTools {

    private static final String TAG = "BindButtonTools";

    public static void bind(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();

        try {
            Class<?> bindViewClass = Class.forName(clazz.getName() + AnnotationUtils.classSuffix);
            Method method = bindViewClass.getMethod("bind", clazz);
            method.invoke(bindViewClass.newInstance(), activity);
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            e.printStackTrace();
            Log.e(TAG, "[bind] error.", e);
        }
    }

}
