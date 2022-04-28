package com.example.plantreapp.logs;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.plantreapp.entities.Journal;
import com.example.plantreapp.entities.Log;
import com.example.plantreapp.repository.LogRepository;

import java.util.List;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class LogsViewModel extends AndroidViewModel {

    private static final String TAG = "LogViewModel";
    private MutableLiveData<List<Log>> mutableLiveData;
    private LogRepository repository;
    private int _journalUid = 0; // Maybe the mutable list needs to live in the view model...

    public LogsViewModel(@NonNull Application application, int journalUid) {
        super(application);
        repository = new LogRepository(application.getApplicationContext());
        _journalUid = journalUid;
        repository.findByJournalUID(journalUid, new Continuation<List<? extends Log>>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }

            @Override
            public void resumeWith(@NonNull Object o) {

            }
        });
    }

    public LiveData<List<Log>> getLogList() {
        return repository.getLogs();
    }

    public void deleteLog(Log log) {
        repository.delete(log, _journalUid, new Continuation<Unit>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE; // Default to this value, generated snippet is nul
            }

            @Override
            public void resumeWith(@NonNull Object o) {
                // Handle on delete... display a toast

            }
        });
    }

    public void addLog(Log log) {
        repository.insert(log, _journalUid, new Continuation<Unit>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }

            @Override
            public void resumeWith(@NonNull Object o) {
                // Handle on delete... display a toast

            }
        });
    }
}
