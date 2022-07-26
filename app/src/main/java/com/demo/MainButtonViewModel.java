package com.demo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

public class MainButtonViewModel extends AndroidViewModel {

    private final MutableLiveData<List<MainButton>> mainButtonList;

    public MainButtonViewModel(@NonNull Application application) {
        super(application);
        mainButtonList = new MutableLiveData<>();
    }

    public MutableLiveData<List<MainButton>> getMainButtonList() {
        return mainButtonList;
    }
}
