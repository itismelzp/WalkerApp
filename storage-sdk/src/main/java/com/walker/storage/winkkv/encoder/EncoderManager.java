package com.walker.storage.winkkv.encoder;

import com.walker.storage.winkkv.WinkKV;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 编码器管理类
 * <p>
 * Created by walkerzpli on 2022/1/18.
 */
public class EncoderManager {

    private static final Map<String, WinkKV.Encoder<?>> ENCODERS = new ConcurrentHashMap<>();

    private static volatile EncoderManager INSTANCE;

    public static EncoderManager g() {
        if (INSTANCE == null) {
            synchronized (EncoderManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EncoderManager();
                }
            }
        }
        return INSTANCE;
    }

    private EncoderManager() {
    }

    public void registerEncoder(WinkKV.Encoder<?> encoder) {
        ENCODERS.put(encoder.tag(), encoder);
    }

    public void registerEncoder(List<WinkKV.Encoder<?>> encoders) {
        if (encoders == null) {
            return;
        }
        for (WinkKV.Encoder<?> encoder : encoders) {
            registerEncoder(encoder);
        }
    }

    public void unregisterEncoder(String tag) {
        ENCODERS.remove(tag);
    }

    public void unregisterEncoder(WinkKV.Encoder<?> encoder) {
        unregisterEncoder(encoder.tag());
    }

    public List<WinkKV.Encoder<?>> getEncoders() {
        return new ArrayList<>(ENCODERS.values());
    }

}
