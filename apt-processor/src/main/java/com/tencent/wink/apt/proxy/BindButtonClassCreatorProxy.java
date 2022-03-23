package com.tencent.wink.apt.proxy;


import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.tencent.wink.apt.bean.BindButtonAnnotationInfo;
import com.tencent.wink.apt.utils.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by walkerzpli on 2021/9/27.
 *
 * code:
 *
 * package com.demo;
 *
 * import android.app.Activity;
 * import android.content.Intent;
 * import android.view.View;
 *
 * public class MainActivity$BindButton {
 *
 *     public void bind(Activity host) {
 *         startActivity(host, 11111, "xxxx");
 *         startActivity(host, 22222, "yyyy");
 *     }
 *
 *     private void startActivity(Activity host, int resId, String className) {
 *         host.findViewById(resId).setOnClickListener(new View.OnClickListener() {
 *             @Override
 *             public void onClick(View view) {
 *                 Intent intent = new Intent();
 *                 intent.setClassName(host, className);
 *                 host.startActivity(intent);
 *             }
 *         });
 *     }
 * }
 */
public class BindButtonClassCreatorProxy extends BaseClassCreatorProxy{

    private final Map<Annotation, BindButtonAnnotationInfo> mElements = new HashMap<>();

    public BindButtonClassCreatorProxy(Elements elementUtils, TypeElement mTypeElement) {
        super(elementUtils, mTypeElement);
    }

    @Override
    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(mPackageName).append(";\n\n");
        builder.append("import android.app.Activity;\n");
        builder.append("import android.content.Intent;\n");
        builder.append("import android.view.View;\n\n");

        builder.append("public class ").append(mTargetClassName).append(" {\n\n");
        generateMethods(builder);
        builder.append("}\n");
        return builder.toString();
    }

    private void generateMethods(StringBuilder builder) {
        builder.append(getTabSpace()).append("public void bind(Activity host) {\n");

        for (Annotation annotation : mElements.keySet()) {
            BindButtonAnnotationInfo annotationInfo = mElements.get(annotation);
            String methodCode = String.format(Locale.getDefault(), "\t\tstartActivity(host, %d, \"%s\");\n", annotationInfo.resId, annotationInfo.className);
            builder.append(methodCode);
        }

        builder.append(getTabSpace()).append("}\n");
        builder.append("\n");
        builder.append(getTabSpace()).append("private void startActivity(Activity host, int resId, String className) {\n");
        builder.append(getTabSpace(2)).append("host.findViewById(resId).setOnClickListener(new View.OnClickListener() {\n");
        builder.append(getTabSpace(3)).append("@Override\n");
        builder.append(getTabSpace(3)).append("public void onClick(View view) {\n");
        builder.append(getTabSpace(4)).append("Intent intent = new Intent();\n");
        builder.append(getTabSpace(4)).append("intent.setClassName(host, className);\n");
        builder.append(getTabSpace(4)).append("host.startActivity(intent);\n");
        builder.append(getTabSpace(3)).append("}\n");
        builder.append(getTabSpace(2)).append("});\n");
        builder.append(getTabSpace()).append("}\n");
    }

    public TypeSpec generateJavaCodeByJavapoet() {
        return TypeSpec.classBuilder(mTargetClassName)
                .addModifiers(Modifier.PRIVATE)
                .addMethod(generateMethodsByJavapoet())
                .build();
    }

    private MethodSpec generateMethodsByJavapoet() {
        return null;
    }

    public void putElement(Annotation annotation, BindButtonAnnotationInfo annotationInfo) {
        mElements.put(annotation, annotationInfo);
    }

    @Override
    protected String getSuffix() {
        return AnnotationUtils.classSuffix;
    }

}