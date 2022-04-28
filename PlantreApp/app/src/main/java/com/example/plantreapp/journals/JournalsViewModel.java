package com.example.plantreapp.journals;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.plantreapp.entities.Journal;
import com.example.plantreapp.repository.JournalRepository;

import java.util.List;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class JournalsViewModel extends AndroidViewModel {

    private static final String TAG = "JournalViewModel";
    private MutableLiveData<List<Journal>> mutableLiveData;
    private JournalRepository repository;
    private int _plantUid = 0; // Maybe the mutable list needs to live in the view model...

    public JournalsViewModel(@NonNull Application application, int plantUid) {
        super(application);
        repository = new JournalRepository(application.getApplicationContext());
        _plantUid = plantUid;
        repository.findByPlantUID(plantUid, new Continuation<List<? extends Journal>>() {
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

    public LiveData<List<Journal>> getJournalList() {
        return repository.getJournals();
    }

    // this position variable can it be replaced with the uuid from the list?
    public void deleteJournal(Journal journal) {
        repository.delete(journal, _plantUid, new Continuation<Unit>() {
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

    public void addJournal(Journal journal) {
        repository.insert(journal, _plantUid, new Continuation<Unit>() {
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
