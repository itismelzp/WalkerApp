package com.walker.apt.bean;

/**
 * Created by walkerzpli on 2022/1/24.
 */
public class ColumnUpdateInfo {

    public int dataType;
    public String tableName;
    public String columnName;
    public int updateVersion;
    public int updateType;

    public ColumnUpdateInfo(int updateVersion, int updateType) {
        this.updateVersion = updateVersion;
        this.updateType = updateType;
    }

    @Override
    public String toString() {
        return "ColumnUpdateInfo{" +
                "updateVersion=" + updateVersion +
                ", updateType=" + updateType +
                '}';
    }
}
