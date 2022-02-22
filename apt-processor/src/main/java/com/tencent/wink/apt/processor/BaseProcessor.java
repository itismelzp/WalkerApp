package com.tencent.wink.apt.processor;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * 注解处理基类:
 * <p>
 * 各种Element对齐的类型：
 * <p>
 * ExecutableElement --> Method
 * <p>
 * VariableElement --> Field
 * <p>
 * TypeElement --> Class
 * <p>
 * PackageElement --> Package
 * <p>
 * Created by walkerzpli on 2022/1/23.
 */
abstract class BaseProcessor extends AbstractProcessor {

    private Messager mMessager;
    protected Elements mElementUtils;

    private boolean mProcessDone;

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
        printMessage(Diagnostic.Kind.NOTE, "[getSupportedAnnotationTypes] supportTypes: "
                + Arrays.toString(supportTypes.toArray()));
        return supportTypes;
    }

    protected abstract Class<?>[] getSupportedAnnotation();

    protected abstract String getTag();

    protected void printMessage(Diagnostic.Kind kind, CharSequence msg) {
        mMessager.printMessage(kind, getTag() + " " + msg);
    }

    protected boolean checkCanProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations == null || annotations.isEmpty()) {
            printMessage(Diagnostic.Kind.NOTE, "[checkCanProcess] No annotations ioc annotation in this project.");
            return false;
        }

        if (roundEnv.processingOver()) {
            printMessage(Diagnostic.Kind.NOTE, "[checkCanProcess]: annotations still available after processing over.");
            return false;
        }

        if (mProcessDone) {
            printMessage(Diagnostic.Kind.NOTE, "[checkCanProcess]: annotations still available after writing.");
            return false;
        }
        return true;
    }

    protected void markProcessStatus(boolean isProcessDone) {
        mProcessDone = isProcessDone;
    }

}
