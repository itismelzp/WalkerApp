package com.tencent.wink.apt.spi;

import java.util.List;

/**
 * Created by walkerzpli on 2021/10/15.
 */
public interface Search {
    List<String> searchDoc(String keyWord);
}
