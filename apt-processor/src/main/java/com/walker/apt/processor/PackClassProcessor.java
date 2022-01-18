package com.walker.apt.processor;

import com.google.auto.service.AutoService;
import com.walker.apt.annotation.PackClass;
import com.walker.apt.proxy.EncoderRegisterCreatorProxy;
import com.walker.apt.proxy.PackClassCreatorProxy;

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
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by walkerzpli on 2022/1/14.
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class PackClassProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Elements mElementUtils;
    private Map<String, PackClassCreatorProxy> mProxyMap = new HashMap<>();
    private Set<String> mTypeSet = new HashSet<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        mMessager = processingEnv.getMessager();
        mElementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(PackClass.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        mMessager.printMessage(Diagnostic.Kind.NOTE, "[PackClassProcessor process] start.");
        mProxyMap.clear();

        // 拿到所有被BindButton注解标的元素（这里是属性元素）
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(PackClass.class);

        for (Element element : elements) {
            if (element instanceof TypeElement) {
                TypeElement classElement = (TypeElement) element;
                String fullClassName = classElement.getQualifiedName().toString();
                mMessager.printMessage(Diagnostic.Kind.NOTE, "[PackClassProcessor process] fullClassName: " + fullClassName);
                PackClassCreatorProxy proxy = mProxyMap.get(fullClassName);
                if (proxy == null) {
                    proxy = new PackClassCreatorProxy(mElementUtils, classElement);
                    mProxyMap.put(fullClassName, proxy);
                }
                mTypeSet.add(proxy.getProxyClassFullName());
            }
        }
        createEncoderSourceFile();
        createEncoderRegisterSourceFile();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[PackClassProcessor process] finish.");
        return true;
    }

    /**
     * 通过StringBuilder生成编码器源码
     */
    private void createEncoderSourceFile() {
        // 通过遍历mProxyMap，创建java文件
        for (String key : mProxyMap.keySet()) {
            PackClassCreatorProxy classCreator = mProxyMap.get(key);
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
     * 通过StringBuilder生成编码器注册源码
     */
    private void createEncoderRegisterSourceFile() {

        EncoderRegisterCreatorProxy classCreator
                = new EncoderRegisterCreatorProxy("com.demo.storage", "EncoderUtil", mTypeSet);
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
