package com.demo.storage.utils;

import com.demo.storage.MyObjectEncoder;
import com.demo.storage.MyParcelObject$Encoder;
import com.demo.storage.MyParcelObjectEncoder;
import com.walker.storage.winkkv.encoder.EncoderManager;

/**
 * Created by walkerzpli on 2022/1/18.
 */
public class EncoderUtil {
    public static void init() {
        EncoderManager.g().registerEncoder(MyParcelObject$Encoder.INSTANCE);
        EncoderManager.g().registerEncoder(MyParcelObjectEncoder.INSTANCE);
        EncoderManager.g().registerEncoder(MyObjectEncoder.INSTANCE);
    }
}
