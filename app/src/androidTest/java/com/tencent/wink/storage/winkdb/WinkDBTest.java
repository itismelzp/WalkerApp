package com.tencent.wink.storage.winkdb;

import com.demo.MyApplication;
import com.tencent.wink.storage.winkdb.model.Word;
import com.tencent.wink.storage.winkdb.repository.WordRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * description
 * <p>
 * Created by walkerzpli on 2022/2/27.
 */

@SuppressWarnings("SimplifiableJUnitAssertion")
public class WinkDBTest {

    WordRepository mRepository;

    @Before
    public void init() {
        mRepository = new WordRepository(MyApplication.getInstance());
    }

    @Test
    public void testWAL() {
        List<Word> words = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            words.add(new Word("test_word_" + i));
        }
        mRepository.insertAll(words);
    }

}
