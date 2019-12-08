package com.example.myapplication.activity.ui.travel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TravelViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TravelViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is travel fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}