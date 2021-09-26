package com.walker.apt.library;

import android.app.Activity;

import com.walker.apt.annotation.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public class BindViewTools {

    public static void bind(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();

        try {
            Class<?> bindViewClass = Class.forName(clazz.getName() + AnnotationUtils.classSuffix);
            Method method = bindViewClass.getMethod("bind", activity.getClass());
            method.invoke(bindViewClass.newInstance(), activity);
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
