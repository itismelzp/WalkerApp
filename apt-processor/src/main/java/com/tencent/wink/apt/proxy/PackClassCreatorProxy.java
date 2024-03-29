package com.tencent.wink.apt.proxy;

import com.tencent.wink.apt.annotation.PackClass;
import com.tencent.wink.apt.type.InterfaceType;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * 编码类生成代理，被{@link PackClass}注解标记的类都会生成对象的编码类
 * <p>
 * Created by walkerzpli on 2022/1/14.
 */
public class PackClassCreatorProxy extends BaseClassCreatorProxy {

    @InterfaceType
    private int mInterfaceType;

    public PackClassCreatorProxy(Elements elementUtils, TypeElement classElement, @InterfaceType int interfaceType) {
        super(elementUtils, classElement);
        this.mInterfaceType = interfaceType;
    }

    public String generatePackableJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(mPackageName).append(";\n\n");
        builder.append("import androidx.annotation.NonNull;\n");
        builder.append("import com.tencent.wink.storage.winkkv.WinkKV;\n");
        builder.append("import io.packable.PackDecoder;\n");
        builder.append("import io.packable.PackEncoder;\n\n");

        builder.append(CANNOT_EDIT_DECLARATION);
        builder.append("public class ").append(mTargetClassName).append(" implements WinkKV.Encoder<").append(mOriginClassName).append(">").append(" {\n\n");
        builder.append(getTabSpace()).append("public static ").append(mTargetClassName).append(" INSTANCE = new ").append(mTargetClassName).append("();\n\n");
        generatePackableMethods(builder);
        builder.append("}\n");
        return builder.toString();
    }

    public String generateParcelableJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(mPackageName).append(";\n\n");
        builder.append("import android.os.Parcel;\n");
        builder.append("import android.os.Parcelable;\n");
        builder.append("import com.tencent.wink.storage.winkkv.WinkKV;\n\n");

        builder.append(CANNOT_EDIT_DECLARATION);
        builder.append("public class ").append(mTargetClassName).append(" implements WinkKV.Encoder<").append(mOriginClassName).append(">").append(" {\n\n");
        builder.append(getTabSpace()).append("public static ").append(mTargetClassName).append(" INSTANCE = new ").append(mTargetClassName).append("();\n\n");
        generateParcelableMethods(builder);
        builder.append("}\n");
        return builder.toString();
    }

    @Override
    public String generateJavaCode() {
        if (mInterfaceType == InterfaceType.PARCELABLE_TYPE) {
            return generateParcelableJavaCode();
        } else if (mInterfaceType == InterfaceType.PACKABLE_TYPE) {
            return generatePackableJavaCode();
        }
        return "";
    }

    private void generateParcelableMethods(StringBuilder builder) {
        builder.append(getTabSpace()).append("private ").append(mTargetClassName).append("() {\n");
        builder.append(getTabSpace()).append("}\n\n");

        builder.append(getTabSpace()).append("@Override\n");
        builder.append(getTabSpace()).append("public String tag() {\n");
        builder.append(getTabSpace(2)).append("return \"").append(mTargetClassName).append("\";\n");
        builder.append(getTabSpace()).append("}\n\n");

        builder.append(getTabSpace()).append("@Override\n");
        builder.append(getTabSpace()).append("public byte[] encode(").append(mOriginClassName).append(" obj) {\n");
        builder.append(getTabSpace(2)).append("return marshall(obj);\n");
        builder.append(getTabSpace()).append("}\n\n");

        builder.append(getTabSpace()).append("@Override\n");
        builder.append(getTabSpace()).append("public ").append(mOriginClassName).append(" decode(byte[] bytes, int offset, int length) {\n");
        builder.append(getTabSpace(2)).append("Parcel parcel = unmarshall(bytes, offset, length);\n");
        builder.append(getTabSpace(2)).append("return ").append(mOriginClassName).append(".CREATOR.createFromParcel(parcel);\n");
        builder.append(getTabSpace()).append("}\n\n");

        builder.append(getTabSpace()).append("private byte[] marshall(Parcelable parcelable) {\n");
        builder.append(getTabSpace(2)).append("Parcel parcel = Parcel.obtain();\n");
        builder.append(getTabSpace(2)).append("parcel.setDataPosition(0);\n");
        builder.append(getTabSpace(2)).append("parcelable.writeToParcel(parcel, 0);\n");
        builder.append(getTabSpace(2)).append("byte[] bytes = parcel.marshall();\n");
        builder.append(getTabSpace(2)).append("parcel.recycle();\n");
        builder.append(getTabSpace(2)).append("return bytes;\n");
        builder.append(getTabSpace()).append("}\n\n");

        builder.append(getTabSpace()).append("private Parcel unmarshall(byte[] bytes, int offset, int length) {\n");
        builder.append(getTabSpace(2)).append("Parcel parcel = Parcel.obtain();\n");
        builder.append(getTabSpace(2)).append("parcel.unmarshall(bytes, offset, length);\n");
        builder.append(getTabSpace(2)).append("parcel.setDataPosition(0);\n");
        builder.append(getTabSpace(2)).append("return parcel;\n");
        builder.append(getTabSpace()).append("}\n\n");

    }

    private void generatePackableMethods(StringBuilder builder) {
        builder.append(getTabSpace()).append("private ").append(mTargetClassName).append("() {\n");
        builder.append(getTabSpace()).append("}\n\n");

        builder.append(getTabSpace()).append("@Override\n");
        builder.append(getTabSpace()).append("public String tag() {\n");
        builder.append(getTabSpace(2)).append("return \"").append(mTargetClassName).append("\";\n");
        builder.append(getTabSpace()).append("}\n\n");

        builder.append(getTabSpace()).append("@Override\n");
        builder.append(getTabSpace()).append("public byte[] encode(@NonNull ").append(mOriginClassName).append(" obj) {\n");
        builder.append(getTabSpace(2)).append("return PackEncoder.marshal(obj);\n");
        builder.append(getTabSpace()).append("}\n\n");

        builder.append(getTabSpace()).append("@Override\n");
        builder.append(getTabSpace()).append("public ").append(mOriginClassName).append(" decode(@NonNull byte[] bytes, int offset, int length) {\n");
        builder.append(getTabSpace(2)).append("return PackDecoder.unmarshal(bytes, offset, length, ").append(mOriginClassName).append(".CREATOR);\n");
        builder.append(getTabSpace()).append("}\n\n");
    }

    @Override
    protected String getSuffix() {
        return "$Encoder";
    }
}
