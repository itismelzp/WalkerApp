package com.walker.apt.spi;

import java.util.List;

/**
 * Created by walkerzpli on 2021/10/15.
 */
public class DatabaseSearch implements Search {
    @Override
    public List<String> searchDoc(String keyWord) {
        System.out.println("数据搜索: " + keyWord);
        return null;
    }
}