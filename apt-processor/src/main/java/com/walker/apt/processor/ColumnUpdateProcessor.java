package com.walker.apt.processor;

import com.google.auto.service.AutoService;
import com.walker.apt.annotation.ColumnUpdate;
import com.walker.apt.bean.ColumnUpdateInfo;
import com.walker.apt.proxy.ColumnUpdateCreatorProxy;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * entity属性升级注解
 * <p>
 * Created by walkerzpli on 2022/1/23.
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ColumnUpdateProcessor extends BaseProcessor {

    private final Map<String, ColumnUpdateCreatorProxy> mProxyMap = new HashMap<>();

    @Override
    protected Class<?>[] getSupportedAnnotation() {
        return new Class<?>[]{ColumnUpdate.class};
    }

    @Override
    protected String getTag() {
        return "ColumnUpdateProcessor";
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        printMessage(Diagnostic.Kind.NOTE, "[process] start.");
        if (!checkCanProcess(annotations, roundEnv)) {
            return false;
        }

        long start = System.currentTimeMillis();

        // 拿到所有被ColumnUpdate注解标的元素（这里是属性元素）
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ColumnUpdate.class);

        for (Element element : elements) {
            if (element instanceof VariableElement) {
                // 1）拿到注解对应的属性元素
                VariableElement variableElement = (VariableElement) element;

                // 2）拿到注解所在的类元素
                TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
//                getTableName(entityElements, classElement);

                String fullClassName = classElement.toString();
                printMessage(Diagnostic.Kind.NOTE, "[process] fullClassName: " + fullClassName);

                // 3）拿到注解对象和属性值
                ColumnUpdate annotation = variableElement.getAnnotation(ColumnUpdate.class);
                int updateVersion = annotation.updateVersion();
                int updateType =  annotation.updateType();
                ColumnUpdateInfo columnUpdateInfo = new ColumnUpdateInfo(updateVersion, updateType);

                // 4）组装CreatorProxy
                ColumnUpdateCreatorProxy proxy = mProxyMap.get(fullClassName);
                if (proxy == null) {
                    proxy = new ColumnUpdateCreatorProxy(mElementUtils, classElement, columnUpdateInfo);
                    mProxyMap.put(fullClassName, proxy);
                }
            }
        }

        createSourceFile();
        markProcessStatus(true);
        printMessage(Diagnostic.Kind.NOTE, "[process] finish, cost: " + (System.currentTimeMillis() - start));
        return true;
    }

    private String getTableName(Set<? extends Element> entityElements, TypeElement classElement) {
        if (entityElements == null || entityElements.size() == 0) {
            return null;
        }

        printMessage(Diagnostic.Kind.NOTE, "[getTableName] classElement: " + classElement.toString());
        for (Element entityElement : entityElements) {

            printMessage(Diagnostic.Kind.NOTE, "[getTableName] entityElement: " + entityElement.toString());
//            if (entityElement.getSimpleName().equals(classElement.getSimpleName())) {
//
//            }
        }
        return null;
    }

    // 通过StringBuilder生成源码
    private void createSourceFile() {
        // 通过遍历mProxyMap，创建java文件
        for (String key : mProxyMap.keySet()) {
            ColumnUpdateCreatorProxy classCreator = mProxyMap.get(key);
            printMessage(Diagnostic.Kind.NOTE, "[createSourceFile] " + classCreator.getProxyClassFullName());
            Writer writer = null;
            try {
                JavaFileObject jfo = processingEnv.getFiler()
                        .createSourceFile(classCreator.getProxyClassFullName(), classCreator.getTypeElement());
                writer = jfo.openWriter();
                writer.write(classCreator.generateJavaCode());
                writer.flush();
            } catch (IOException e) {
                printMessage(Diagnostic.Kind.NOTE,
                        "[createSourceFile] " + classCreator.getProxyClassFullName() + "error");
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        printMessage(Diagnostic.Kind.ERROR,
                                "[createSourceFile] " + classCreator.getProxyClassFullName() + " error");
                    }
                }
            }
        }
    }

}
