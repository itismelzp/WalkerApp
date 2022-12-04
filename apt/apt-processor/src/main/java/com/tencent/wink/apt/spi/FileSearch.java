package com.tencent.wink.apt.spi;

import java.util.List;

/**
 * Created by walkerzpli on 2021/10/15.
 */
public class FileSearch implements Search {
    @Override
    public List<String> searchDoc(String keyWord) {
        System.out.println("文件搜索: " + keyWord);
        return null;
    }
}
