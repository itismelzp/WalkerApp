package com.demo.apt.autel;

/**
 * Created by lizhiping on 2022/10/1.
 * <p>
 * description
 */
public enum DroneParameterType {

    UNKOWN(0),
    BOOL(1),
    INT32(2),
    INT64(3),
    FLOAT(4),
    DOUBLE(5),
    STRING(6),
    ANY_DATA(7),
    UNRECOGNIZED(-1);

    DroneParameterType(int i) {
    }

}
