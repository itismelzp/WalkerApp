package com.tencent.wink.apt.proxy;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.tencent.wink.apt.utils.AnnotationUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public class ClassCreatorProxy extends BaseClassCreatorProxy {

    private final Map<Integer, VariableElement> mVariableElementMap = new HashMap<>();

    public ClassCreatorProxy(Elements elementUtils, TypeElement classElement) {
        super(elementUtils, classElement);
    }

    public void putElement(int id, VariableElement element) {
        mVariableElementMap.put(id, element);
    }

    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(mPackageName).append(";\n\n");
        builder.append("import com.tencent.wink.apt.library.*;\n\n");
        builder.append("public class ").append(mTargetClassName).append(" {\n");
        generateMethods(builder);
        builder.append("}\n");
        return builder.toString();
    }

    public TypeSpec generateJavaCodeByJavapoet() {
        return TypeSpec.classBuilder(mTargetClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(generateMethodsByJavapoet())
                .build();
    }

    private void generateMethods(StringBuilder builder) {
        builder.append(String.format("public void bind(%s host) {\n", mTypeElement.getQualifiedName()));
        for (int id : mVariableElementMap.keySet()) {
            VariableElement element = mVariableElementMap.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();
            String methodCode = String.format(Locale.getDefault(),
                    getTabSpace(2) + "host.%s = (%s)(((android.app.Activity) host).findViewById(%d));\n",
                    name, type, id);
            builder.append(methodCode);
        }
        builder.append(getTabSpace()).append("}\n");
    }

    private MethodSpec generateMethodsByJavapoet() {
        ClassName host = ClassName.bestGuess(mTypeElement.getQualifiedName().toString());
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(host, "host");

        for (int id : mVariableElementMap.keySet()) {
            VariableElement element = mVariableElementMap.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();
            String methodCode = String.format(Locale.getDefault(),
                    "host.%s = (%s)(((android.app.Activity) host).findViewById(%d));\n", name, type, id);
            methodBuilder.addCode(methodCode);
        }

        return methodBuilder.build();
    }

    @Override
    protected String getSuffix() {
        return AnnotationUtils.classSuffix;
    }

}
