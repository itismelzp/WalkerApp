package com.tencent.wink.apt.proxy;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by walkerzpli on 2022/1/19.
 */
public abstract class BaseClassCreatorProxy {

    private static final String TAB_SPACE = "    ";

    protected String mOriginClassName;
    protected String mTargetClassName;
    protected String mPackageName;
    protected Elements mElementUtils;
    protected TypeElement mTypeElement;
    protected static final String CANNOT_EDIT_DECLARATION = "/**\n * This class is generated by IDE, do not edit.\n */\n";

    public BaseClassCreatorProxy() {
    }

    public BaseClassCreatorProxy(Elements mElementUtils, TypeElement mTypeElement) {
        this.mElementUtils = mElementUtils;
        this.mTypeElement = mTypeElement;
        this.mPackageName = getPackageName(mElementUtils, mTypeElement);
        this.mOriginClassName = getOriginClassName(mTypeElement);
        this.mTargetClassName = getTargetClassName(mTypeElement);
    }

    public String getProxyClassFullName() {
        return mPackageName + "." + mTargetClassName;
    }

    public TypeElement getTypeElement() {
        return mTypeElement;
    }

    public String getPackageName() {
        return mPackageName;
    }

    protected String getPackageName(Elements elementUtils, TypeElement typeElement) {
        PackageElement packageElement = elementUtils.getPackageOf(typeElement);
        return packageElement.getQualifiedName().toString();
    }

    protected String getOriginClassName(TypeElement classElement) {
        return classElement.getQualifiedName().toString();
    }

    protected String getTargetClassName(TypeElement classElement) {
        return classElement.getSimpleName().toString() + getSuffix();
    }

    protected abstract String getSuffix();

    public abstract String generateJavaCode();

    protected String getTabSpace() {
        return getTabSpace(1);
    }

    protected String getTabSpace(int count) {
        if (count < 1) {
            return "";
        }
        StringBuilder tabSpace = new StringBuilder();
        for (int i = 0; i < count; i++) {
            tabSpace.append(TAB_SPACE);
        }
        return tabSpace.toString();
    }

}
