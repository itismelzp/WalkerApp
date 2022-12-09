package com.tencent.wink.apt.processor;

import com.google.auto.service.AutoService;
import com.tencent.wink.apt.annotation.PackClass;
import com.tencent.wink.apt.proxy.EncoderRegisterCreatorProxy;
import com.tencent.wink.apt.proxy.PackClassCreatorProxy;
import com.tencent.wink.apt.type.InterfaceType;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * PackClass注解处理类
 * <p>
 * Created by walkerzpli on 2022/1/14.
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class PackClassProcessor extends BaseProcessor {

    private final Map<String, PackClassCreatorProxy> mProxyMap = new HashMap<>();
    private final Set<String> mClassFullNameSet = new HashSet<>();

    @Override
    protected Class<?>[] getSupportedAnnotation() {
        return new Class[]{PackClass.class};
    }

    @Override
    protected String getTag() {
        return "PackClassProcessor";
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!checkCanProcess(annotations, roundEnv)) {
            return false;
        }

        long start = System.currentTimeMillis();
        printMessage(Diagnostic.Kind.NOTE, getTag() + "[process] start.");
        mProxyMap.clear();

        // 拿到所有被PackClass注解标的元素（这里是type元素）
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(PackClass.class);

        for (Element element : elements) {
            if (element instanceof TypeElement) {
                TypeElement classElement = (TypeElement) element;
                String fullClassName = classElement.getQualifiedName().toString();
                printMessage(Diagnostic.Kind.NOTE, "[PackClassProcessor process] fullClassName: " + fullClassName);
                PackClassCreatorProxy proxy = mProxyMap.get(fullClassName);
                if (proxy == null) {
                    proxy = new PackClassCreatorProxy(mElementUtils, classElement, getInterfaceType(classElement));
                    mProxyMap.put(fullClassName, proxy);
                }
                mClassFullNameSet.add(proxy.getProxyClassFullName());
            }
        }
        createEncoderSourceFile();
        createEncoderRegisterSourceFile();

        long end = System.currentTimeMillis();
        printMessage(Diagnostic.Kind.NOTE, "[PackClassProcessor process] finish, cost: " + (end - start) + "ms");
        markProcessStatus(true);
        return true;
    }

    private int getInterfaceType(TypeElement classElement) {
        List<? extends TypeMirror> interfaces = classElement.getInterfaces();
        for (TypeMirror typeMirror : interfaces) {
            printMessage(Diagnostic.Kind.NOTE, "[getInterfacesType] typeMirror: " + typeMirror.toString());
            if (typeMirror.toString().contains("android.os.Parcelable")) {
                return InterfaceType.PARCELABLE_TYPE;
            } else if (typeMirror.toString().contains("io.packable.Packable")) {
                return InterfaceType.PACKABLE_TYPE;
            }
        }
        return InterfaceType.NONE_TYPE;
    }

    /**
     * 通过StringBuilder生成编码器源码
     */
    private void createEncoderSourceFile() {
        // 通过遍历mProxyMap，创建java文件
        for (String key : mProxyMap.keySet()) {
            PackClassCreatorProxy classCreator = mProxyMap.get(key);
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

    /**
     * 通过StringBuilder生成编码器注册源码
     */
    private void createEncoderRegisterSourceFile() {

        EncoderRegisterCreatorProxy classCreator = new EncoderRegisterCreatorProxy(mClassFullNameSet);
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
