package com.demo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * Created by lizhiping on 2023/1/17.
 * <p>
 * description
 */
class FilterViewModel(application: Application) : AndroidViewModel(application) {

    val filterList: MutableLiveData<List<MainButton>> = MutableLiveData()

}