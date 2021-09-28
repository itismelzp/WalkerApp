package com.walker.apt.processor;


import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.walker.apt.utils.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by walkerzpli on 2021/9/27.
 */
public class BindButtonClassCreatorProxy {

    private String mClassName;
    private String mPackageName;
    private TypeElement mTypeElement;
    //    private Map<Integer, VariableElement> mVariableElements = new HashMap<>();
//    private Map<Class<?>, VariableElement> mClassElements = new HashMap<>();
    private Map<Annotation, BindButtonAnnotationInfo> mElements = new HashMap<>();

    public BindButtonClassCreatorProxy(Elements elementUtils, TypeElement mTypeElement) {
        this.mTypeElement = mTypeElement;
        PackageElement packageElement = elementUtils.getPackageOf(mTypeElement);
        String packageName = packageElement.getQualifiedName().toString();
        String className = mTypeElement.getSimpleName().toString();
        this.mPackageName = packageName;
        this.mClassName = className + AnnotationUtils.classSuffix;
    }

    /**
     * code:
     * private void initButton(int resId, final Class<?> clz) {
     *   findViewById(resId).setOnClickListener(view -> startActivity(clz));
     * }
     * @return
     */
    public TypeSpec generateJavaCodeByJavapoet() {
        return TypeSpec.classBuilder(mClassName)
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

    public String getPackageName() {
        return mPackageName;
    }

}