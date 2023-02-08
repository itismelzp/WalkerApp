package com.demo.album;


import java.io.File;

public class TextUtil {

    public static String getPathSuffix(String path) {
        if ((path != null) && !path.isEmpty()) {
            try {
                return path.substring(path.lastIndexOf(File.separator) + 1);
            } catch (Exception e) {
            }
        }
        return null;
    }
}