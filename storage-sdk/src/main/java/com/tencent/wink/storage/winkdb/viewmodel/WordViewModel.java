package com.tencent.wink.storage.winkdb.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.tencent.wink.storage.winkdb.model.Word;
import com.tencent.wink.storage.winkdb.model.WordWrapper;
import com.tencent.wink.storage.winkdb.repository.WordRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by walkerzpli on 2021/8/5.
 */
public class WordViewModel extends AndroidViewModel {

    private final WordRepository mRepository;

    private final LiveData<List<Word>> mAllWords;
    private final LiveData<List<WordWrapper>> mAllWordWrappers;

    public WordViewModel(Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
        mAllWordWrappers = Transformations.map(mAllWords, this::wrapWords);
    }

    private List<WordWrapper> wrapWords(List<Word> words) {
        List<WordWrapper> wordWrappers = new ArrayList<>();
        for (Word word : words) {
            WordWrapper wordWrapper = wrapWord(word);
            wordWrappers.add(wordWrapper);
        }
        return wordWrappers;
    }

    private WordWrapper wrapWord(Word word) {
        WordWrapper wordWrapper = new WordWrapper();
        wordWrapper.mDescription = String.format(Locale.getDefault(),
                "content: %s, createTime: %d", word.getContent(), word.getCreateTime());
        return wordWrapper;
    }

    public List<String> wordWrappers2Str(List<WordWrapper> wrappers) {
        List<String> stringList = new ArrayList<>();
        for (WordWrapper wrapper : wrappers) {
            stringList.add("-> " + wrapper.mDescription + " <-");
        }
        return stringList;
    }

    private String wordWrapper2Str(WordWrapper wordWrapper) {
        if (wordWrapper == null) {
            return "null";
        }
        return "->" + wordWrapper.mDescription + "<-";
    }

    public LiveData<Word> getLastWord() {
        return mRepository.getLastWord();
    }

    public LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    public LiveData<List<WordWrapper>> getAllWordsWrapper() {
        return mAllWordWrappers;
    }

    public LiveData<List<String>> getAllWordWrapperStr() {
        return Transformations.map(mAllWordWrappers, this::wordWrappers2Str);
    }

    public void insert(Word word) {
        mRepository.insert(word);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void delete(String word) {
        mRepository.delete(word);
    }

    public void delete(String... words) {
        mRepository.delete(words);
    }

    public void delete(List<String> words) {
        mRepository.delete(words);
    }

    public void deleteFuzzy(String word) {
        mRepository.deleteFuzzy(word);
    }

    public void update(Word word) {
        mRepository.update(word);
    }

}
