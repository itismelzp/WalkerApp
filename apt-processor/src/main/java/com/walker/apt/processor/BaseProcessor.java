package com.walker.apt.processor;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;

/**
 * 注解处理基类
 * <p>
 * Created by walkerzpli on 2022/1/23.
 */
abstract class BaseProcessor extends AbstractProcessor {

    protected Messager mMessager;
    protected Elements mElementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mElementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportTypes = new LinkedHashSet<>();
        Class<?>[] supportedAnnotation = getSupportedAnnotation();

        if (supportedAnnotation == null || supportedAnnotation.length == 0) {
            return new LinkedHashSet<>();
        }
        for (Class<?> aClass : supportedAnnotation) {
            supportTypes.add(aClass.getCanonicalName());
        }
        return supportTypes;
    }

    protected abstract Class<?>[] getSupportedAnnotation();

}
