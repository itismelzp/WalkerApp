package com.tencent.wink.apt.proxy;

import java.util.Set;

/**
 * 解码器注册类生成代理
 * <p>
 * Created by walkerzpli on 2022/1/18.
 */
public class EncoderRegisterCreatorProxy extends BaseClassCreatorProxy {

    private static final String TARGET_CLASS_NAME = "EncoderUtil";
    private static final String PACKAGE_NAME = "com.demo.storage";
    private final Set<String> mEncoderClassFullName;

    public EncoderRegisterCreatorProxy(Set<String> encoders) {
        this.mEncoderClassFullName = encoders;
        this.mPackageName = PACKAGE_NAME;
        this.mTargetClassName = TARGET_CLASS_NAME;
    }

    @Override
    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(mPackageName).append(";\n\n");
        generateImport(builder);
        builder.append("import com.tencent.wink.storage.winkkv.encoder.EncoderManager;\n");

        builder.append(CANNOT_EDIT_DECLARATION);
        builder.append("public class ").append(mTargetClassName).append(" {").append('\n');
        generateMethods(builder);
        builder.append("}\n");
        return builder.toString();
    }

    private void generateImport(StringBuilder builder) {
        for (String encoder : mEncoderClassFullName) {
            builder.append("import ").append(encoder).append(";\n");
        }
    }

    private void generateRegisterCode(StringBuilder builder) {
        for (String encoder : mEncoderClassFullName) {
            builder.append(getTabSpace(2))
                    .append("EncoderManager.g().registerEncoder(").append(encoder).append(".INSTANCE);").append('\n');
        }
    }

    private void generateMethods(StringBuilder builder) {
        builder.append(getTabSpace()).append("public static void init() {").append('\n');
        generateRegisterCode(builder);
        builder.append(getTabSpace()).append("}").append('\n');
    }

    @Override
    protected String getSuffix() {
        return "";
    }

}
