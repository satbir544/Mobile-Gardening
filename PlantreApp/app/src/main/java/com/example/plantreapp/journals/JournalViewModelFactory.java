package com.example.plantreapp.journals;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class JournalViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private int mPlantUid;

    public JournalViewModelFactory(Application application, int plant_uid) {
        mApplication = application;
        mPlantUid = plant_uid;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new JournalsViewModel(mApplication, mPlantUid);
    }
}
