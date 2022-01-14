package com.walker.apt.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.walker.apt.annotation.BindButton;
import com.walker.apt.bean.BindButtonAnnotationInfo;
import com.walker.apt.proxy.BindButtonClassCreatorProxy;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

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

    /**
     * 各种Element对齐的类型：
     * <p>
     * ExecutableElement --> Method
     * <p>
     * VariableElement --> Field
     * <p>
     * TypeElement --> Class
     * <p>
     * PackageElement --> Package
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
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
            String className = getClassFromAnnotation(variableElement);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "[process] resId: " + resId);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "[process] className: " + className);
            if (className == null) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, "[process] className should not be null.");
                return false;
            }
            BindButtonAnnotationInfo annotationInfo = new BindButtonAnnotationInfo(resId, className);
            proxy.putElement(annotation, annotationInfo);
        }

        createSourceFile();
//        createSourceFileByJavapoet();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[process] finish.");
        return true;
    }

    private String getClassFromAnnotation(Element key) {
        List<? extends AnnotationMirror> annotationMirrors = key.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            if (BindButton.class.getName().equals(annotationMirror.getAnnotationType().toString())) {
                Set<? extends ExecutableElement> keySet = annotationMirror.getElementValues().keySet();
                for (ExecutableElement executableElement : keySet) {
                    if (Objects.equals(executableElement.getSimpleName().toString(), "clazz")) {
                        return annotationMirror.getElementValues().get(executableElement).getValue().toString();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 通过StringBuilder生成源码
     */
    private void createSourceFile() {
        // 通过遍历mProxyMap，创建java文件
        for (String key : mProxyMap.keySet()) {
            BindButtonClassCreatorProxy classCreator = mProxyMap.get(key);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "[createSourceFile] " + classCreator.getProxyClassFullName());
            Writer writer = null;
            try {
                JavaFileObject jfo = processingEnv.getFiler()
                        .createSourceFile(classCreator.getProxyClassFullName(), classCreator.getTypeElement());
                writer = jfo.openWriter();
                writer.write(classCreator.generateJavaCode());
                writer.flush();
            } catch (IOException e) {
                mMessager.printMessage(Diagnostic.Kind.NOTE,
                        "[createSourceFile] " + classCreator.getProxyClassFullName() + "error");
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        mMessager.printMessage(Diagnostic.Kind.ERROR,
                                "[createSourceFile] " + classCreator.getProxyClassFullName() + " error");
                    }
                }
            }
        }
    }

    private void createSourceFileByJavapoet() {
        for (BindButtonClassCreatorProxy creatorProxy : mProxyMap.values()) {

            mMessager.printMessage(Diagnostic.Kind.NOTE, "[createSourceFileByJavapoet] getPackageName: "+creatorProxy.getPackageName());
//            mMessager.printMessage(Diagnostic.Kind.NOTE, "[createSourceFileByJavapoet] process finish.");

            JavaFile javaFile = JavaFile
                    .builder(creatorProxy.getPackageName(), creatorProxy.generateJavaCodeByJavapoet())
                    .build();
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
