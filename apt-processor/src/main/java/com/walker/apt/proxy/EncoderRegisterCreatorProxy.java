package com.walker.apt.proxy;

import javax.lang.model.element.TypeElement;

/**
 * Created by walkerzpli on 2022/1/18.
 */
public class EncoderRegisterCreatorProxy {

    public static final String SUFFIX = "";

    private static final String TAB_SPACE = "    ";

    private String mOriginClassName;
    private String mTargetClassName;
    private String mPackageName;
    private TypeElement mTypeElement;

    public EncoderRegisterCreatorProxy(String packageName, String className) {
        this.mPackageName = packageName;
        this.mOriginClassName = className;
        this.mTargetClassName = className + SUFFIX;

    }

    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        // TODO::
        generateMethods(builder);
        return builder.toString();
    }

    private void generateMethods(StringBuilder builder) {
        //TODO::
    }

    public String getProxyClassFullName() {
        return mPackageName + "." + mTargetClassName;
    }

    public TypeElement getTypeElement() {
        return mTypeElement;
    }

}
