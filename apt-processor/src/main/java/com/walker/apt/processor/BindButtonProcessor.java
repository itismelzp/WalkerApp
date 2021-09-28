package com.walker.apt.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.walker.apt.annotation.BindButton;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BindButtonProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Elements mElementUtils;
    private Map<String, BindButtonClassCreatorProxy> mProxyMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(BindButton.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        /**
         * 各种Element对齐的类型：
         * ExecutableElement --> Method
         * VariableElement --> Field
         * TypeElement --> Class
         * PackageElement --> Package
         */
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[process] start.");
        mProxyMap.clear();

        // 拿到所有被BindButton注解标的元素（这里是属性元素）
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindButton.class);
        for (Element element : elements) {
            // 1）实际是属性元素
            VariableElement variableElement = (VariableElement) element;
            // 2）拿到属性所在的类元素
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
            String fullClassName = classElement.getQualifiedName().toString();
            mMessager.printMessage(Diagnostic.Kind.NOTE, "[process] fullClassName: " + fullClassName);
            BindButtonClassCreatorProxy proxy = mProxyMap.get(fullClassName);
            if (proxy == null) {
                proxy = new BindButtonClassCreatorProxy(mElementUtils, classElement);
                mProxyMap.put(fullClassName, proxy);
            }
            // 拿到属性对应的注解对象
            BindButton annotation = variableElement.getAnnotation(BindButton.class);
            int resId = annotation.resId();
            Class<?> clazz = annotation.clazz();
            BindButtonAnnotationInfo annotationInfo = new BindButtonAnnotationInfo(resId, clazz);
            proxy.putElement(annotation, annotationInfo);
        }

        createSourceFileByJavapoet();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[process] finish.");
        return true;
    }

    private void createSourceFileByJavapoet() {
        for (BindButtonClassCreatorProxy creatorProxy : mProxyMap.values()) {
            JavaFile javaFile = JavaFile.builder(creatorProxy.getPackageName(),
                    creatorProxy.generateJavaCodeByJavapoet()).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
                mMessager.printMessage(Diagnostic.Kind.ERROR,
                        "[createSourceFileByJavapoet] process failed: " + e.getMessage());

            }
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[createSourceFileByJavapoet] process finish.");

    }
}
