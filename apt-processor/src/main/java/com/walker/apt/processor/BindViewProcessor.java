package com.walker.apt.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.walker.apt.annotation.BindView;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@SupportedAnnotationTypes({"com.walker.apt.annotation.BindView"})
public class BindViewProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Elements mElementUtils;
    private Map<String, ClassCreatorProxy> mProxyMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mElementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(BindView.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 根据注解生成Java文件
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[process] start.");
        mProxyMap.clear();

        // 得到所有被BindView注解标注的元素（这里是属性元素）
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        for (Element element : elements) {
            VariableElement variableElement = (VariableElement) element;
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
            String fullClassName = classElement.getQualifiedName().toString();
            ClassCreatorProxy proxy = mProxyMap.get(fullClassName);
            if (proxy == null) {
                proxy = new ClassCreatorProxy(mElementUtils, classElement);
                mProxyMap.put(fullClassName, proxy);
            }
            BindView bindViewAnnotation = variableElement.getAnnotation(BindView.class);
            int id = bindViewAnnotation.value();
            proxy.putElement(id, variableElement);
        }

//        createSourceFile();
        createSourceFileByJavapoet();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[process] finish.");
        return true;
    }

    /**
     * 通过StringBuilder生成源码
     */
    private void createSourceFile() {
        // 通过遍历mProxyMap，创建java文件
        for (String key : mProxyMap.keySet()) {
            ClassCreatorProxy classCreator = mProxyMap.get(key);
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

    /**
     * 通过javapoet生成源码
     */
    private void createSourceFileByJavapoet() {
        for (String key : mProxyMap.keySet()) {
            ClassCreatorProxy classCreator = mProxyMap.get(key);
            JavaFile javaFile = JavaFile.builder(classCreator.getPackageName(),
                    classCreator.generateJavaCodeByJavapoet()).build();
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