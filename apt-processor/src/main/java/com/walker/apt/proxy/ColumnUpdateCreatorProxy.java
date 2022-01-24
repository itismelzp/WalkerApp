package com.walker.apt.proxy;

import com.walker.apt.bean.ColumnUpdateInfo;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by walkerzpli on 2022/1/24.
 */
public class ColumnUpdateCreatorProxy extends BaseClassCreatorProxy {

    private ColumnUpdateInfo mColumnUpdateInfo;

    public ColumnUpdateCreatorProxy(Elements elementUtils, TypeElement classElement, ColumnUpdateInfo columnUpdateInfo) {
        super(elementUtils, classElement);
        this.mColumnUpdateInfo = columnUpdateInfo;
    }

    @Override
    protected String getSuffix() {
        return "_migration";
    }

    @Override
    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(mPackageName).append(";\n");
        builder.append("import androidx.annotation.NonNull;\n");
        builder.append("import androidx.room.migration.Migration;\n");
        builder.append("import androidx.sqlite.db.SupportSQLiteDatabase;\n\n");
        builder.append("public class ").append(mTargetClassName).append(" {\n");
        builder.append("    public static final Migration MIGRATION_1_").append(mColumnUpdateInfo.updateVersion).append(" = new Migration(1, ").append(mColumnUpdateInfo.updateVersion).append(") {\n");
        builder.append("        @Override\n");
        builder.append("        public void migrate(@NonNull SupportSQLiteDatabase database) {\n");
        builder.append("            database.execSQL(\" ").append(generaAddSQL()).append("\");\n");
        builder.append("         }\n");
        builder.append("    };\n");
        builder.append("}\n");
        return builder.toString();
    }

    private String generaAddSQL() {
        return "ALTER TABLE " + mColumnUpdateInfo.tableName + " ADD COLUMN " + mColumnUpdateInfo.columnName + " " + mColumnUpdateInfo.updateType + " NOT NULL";
    }

}
