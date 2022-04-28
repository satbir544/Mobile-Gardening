package com.example.plantreapp.myPlants;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.plantreapp.entities.Plant;
import com.example.plantreapp.entities.PlantIdentity;
import com.example.plantreapp.repository.PlantIdentityRepository;
import com.example.plantreapp.repository.PlantRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

// Extends AndroidViewModel to get the Context
public class PlantsViewModel extends AndroidViewModel {
    private PlantRepository repository;
    private PlantIdentityRepository plantIdentityRepository;

    public PlantsViewModel(@NonNull Application application) {
        super(application);
        repository = new PlantRepository(application.getApplicationContext());
        plantIdentityRepository = new PlantIdentityRepository(application.getApplicationContext());
    }

    // Every LiveData<Plant, Journal, Logs> - Should be able to be replaced with my files in the entities folder
    // ... You should be able to delete all the models that satbir has and replace them with the ones in entities.
    public LiveData<List<Plant>> getPlantList() {
        return repository.getPlants();
    }
    public LiveData<List<PlantIdentity>> getPlantIdentityList() { return plantIdentityRepository.getPlantIDs(); }

    public void updatePlantIdentity(PlantIdentity plantIdentity) {
        plantIdentityRepository.update(plantIdentity, new Continuation<Unit>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return null;
            }

            @Override
            public void resumeWith(@NonNull Object o) {

            }
        });
    }

    public void updatePlant(Plant plant) {
        repository.updatePlant(plant, new Continuation<Unit>() {
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

    public void updatePlantPosition(Plant plant, int position) {
        repository.updatePlantPosition(plant, position, new Continuation<Unit>() {
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

    public void addPlantIdentity(PlantIdentity plantIdentity, int plant_uid) {
        plantIdentityRepository.insert(plantIdentity,new Continuation<Unit>() {
            @Override
            public void resumeWith(@NonNull Object o) {

            }

            @NonNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE; // Default to this value, generated snippet is nul
            }
        });
    }

    public void deletePlantIdentity(PlantIdentity plantIdentity, int plant_uid) {
        plantIdentityRepository.delete(plantIdentity, plant_uid, new Continuation<Unit>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE; // Default to this value, generated snippet is nul
            }

            @Override
            public void resumeWith(@NonNull Object o) {

            }
        });
    }


    public void deletePlant(Plant plant) {

        repository.delete(plant, new Continuation<Unit>() {
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

    public void addPlant(Plant plant) {
        repository.insert(plant, new Continuation<Unit>() {
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
