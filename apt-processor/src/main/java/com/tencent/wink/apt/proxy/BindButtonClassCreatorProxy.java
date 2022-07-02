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
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by walkerzpli on 2021/9/27.
 * <p>
 * code:
 * <p>
 * package com.demo;
 * <p>
 * import android.app.Activity;
 * import android.content.Intent;
 * import android.view.View;
 * <p>
 * public class MainActivity$BindButton {
 * <p>
 *     public void bind(Activity host) {
 *         startActivity(host, 11111, "xxxx");
 *         startActivity(host, 22222, "yyyy");
 *     }
 * <p>
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
public class BindButtonClassCreatorProxy extends BaseClassCreatorProxy {

    private final Map<Annotation, BindButtonAnnotationInfo> mElements = new HashMap<>();

    public BindButtonClassCreatorProxy(Elements elementUtils, TypeElement mTypeElement) {
        super(elementUtils, mTypeElement);
    }

    @Override
    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(mPackageName).append(";\n\n");
        builder.append("import android.content.Intent;\n");
        builder.append("import android.view.View;\n\n");
        builder.append("import java.lang.reflect.Field;\n\n");

        builder.append(CANNOT_EDIT_DECLARATION);
        builder.append("public class ").append(mTargetClassName).append(" {\n\n");
        generateMethods(builder);
        builder.append("}\n");
        return builder.toString();
    }

    private void generateMethods(StringBuilder builder) {
        // public void bind(com.demo.MainActivity) {
        builder.append(getTabSpace()).append("public void bind(").append(mOriginClassName).append(" host) {\n");
        for (Annotation annotation : mElements.keySet()) {
            BindButtonAnnotationInfo annotationInfo = mElements.get(annotation);
            String methodCode = String.format(Locale.getDefault(), getTabSpace(2) + "startActivity(host, %d, \"%s\", %d, \"%s\");\n",
                    annotationInfo.resId, annotationInfo.className, annotationInfo.flag,
                    annotationInfo.filedName);
            builder.append(methodCode);
        }
        builder.append(getTabSpace()).append("}\n");
        builder.append("\n");

        // private void startActivity(com.demo.MainActivity, int, String, int, String) {
        builder.append(getTabSpace()).append("private void startActivity(").append(mOriginClassName).append(" host, int resId, String className, int flag, String fieldName) {\n");
        builder.append(getTabSpace(2)).append("setField(host, fieldName, resId);\n");
        builder.append(getTabSpace(2)).append("host.findViewById(resId).setOnClickListener(new View.OnClickListener() {\n");
        builder.append(getTabSpace(3)).append("@Override\n");
        builder.append(getTabSpace(3)).append("public void onClick(View view) {\n");
        builder.append(getTabSpace(4)).append("Intent intent = new Intent();\n");
        builder.append(getTabSpace(4)).append("intent.setClassName(host, className);\n");
        builder.append(getTabSpace(4)).append("if (flag != -1) {\n");
        builder.append(getTabSpace(5)).append("intent.setFlags(flag);\n");
        builder.append(getTabSpace(4)).append("}\n");
        builder.append(getTabSpace(4)).append("host.startActivity(intent);\n");
        builder.append(getTabSpace(3)).append("}\n");
        builder.append(getTabSpace(2)).append("});\n");
        builder.append(getTabSpace()).append("}\n");

        // private void setField(com.demo.MainActivity, String, int) {
        builder.append("\n");
        builder.append(getTabSpace()).append("private void setField(").append(mOriginClassName).append(" host, String filedName, int resId) {\n");
        builder.append(getTabSpace(2)).append("try {\n");
        builder.append(getTabSpace(3)).append("Field field = host.getClass().getDeclaredField(filedName);\n");
        builder.append(getTabSpace(3)).append("field.setAccessible(true);\n");
        builder.append(getTabSpace(3)).append("field.set(host, host.findViewById(resId));\n");
        builder.append(getTabSpace(2)).append("} catch (NoSuchFieldException | IllegalAccessException e) {\n");
        builder.append(getTabSpace(3)).append("e.printStackTrace();\n");
        builder.append(getTabSpace(2)).append("}\n");
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