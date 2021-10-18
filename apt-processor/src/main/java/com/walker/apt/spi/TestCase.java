package com.walker.apt.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by walkerzpli on 2021/10/15.
 */
public class TestCase {

    public static void main(String[] args) {
        ServiceLoader<Search> s = ServiceLoader.load(Search.class);
        Iterator<Search> iterator = s.iterator();
        while (iterator.hasNext()) {
            Search search = iterator.next();
            search.searchDoc("hello world");
        }
    }
}
