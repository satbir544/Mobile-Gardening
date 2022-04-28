package com.example.plantreapp.logs;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LogViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private int mJournalUid;

    public LogViewModelFactory(Application application, int journal_uid) {
        mApplication = application;
        mJournalUid = journal_uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LogsViewModel(mApplication, mJournalUid);
    }
}
