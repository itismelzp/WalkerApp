package com.walker.storage.room.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.walker.storage.room.model.Word;
import com.walker.storage.room.repository.WordRepository;

import java.util.List;

/**
 * Created by walkerzpli on 2021/8/5.
 */
public class WordViewModel extends AndroidViewModel {

    private final WordRepository mRepository;

    private final LiveData<List<Word>> mAllWords;

    public WordViewModel(Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    public LiveData<Word> getLastWord() {
        return mRepository.getLastWord();
    }

    public LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    public void insert(Word word) {
        mRepository.insert(word);
    }

    public void delete(String word) {
        mRepository.delete(word);
    }

}
