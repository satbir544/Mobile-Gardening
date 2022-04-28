package com.example.plantreapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.plantreapp.dao.PlantDAO
import com.example.plantreapp.db.AppDatabase
import com.example.plantreapp.entities.Plant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlantRepository(context: Context) {
    private var dao: PlantDAO? = null
    var plants: MutableLiveData<List<Plant>> = MutableLiveData()

    init  {
        val db = AppDatabase.invoke(context)
        dao = db.plantDao()
        runBlocking {
            plants.postValue(dao?.getAll())
        }
    }

    suspend fun getAll() : List<Plant>? {
        val list: List<Plant> = emptyList()
        return dao?.getAll() ?: list
    }

    suspend fun findByName(name: String) : List<Plant> {
        val list: List<Plant> = emptyList()
        return dao?.findByName(name) ?: list
    }

    suspend fun updatePlantPosition(plant: Plant, position: Int) {
        runBlocking {
            dao?.clearOldPositions(position)
        }
        runBlocking { 
            dao?.update(plant)
        }
    }

    suspend fun findById(id: Int) : List<Plant> {
        val list: List<Plant> = emptyList()
        return dao?.findById(id) ?: list
    }

    suspend fun findByPosition(position: Int) : Plant? {
        return dao?.findByPosition(position)
    }

    suspend fun insert(plant: Plant) {
       runBlocking { dao?.insert(plant) }
        runBlocking {
            plants.postValue(dao?.getAll())
        }
    }

    suspend fun delete(plant: Plant) {
        runBlocking { dao?.delete(plant) }
        runBlocking {
            plants.postValue(dao?.getAll())
        }
    }

    suspend fun updatePlant(plant: Plant) {
        runBlocking { dao?.update(plant)  }
        runBlocking {
            plants.postValue(dao?.getAll())
        }
    }

    suspend fun insertAll(plantList: List<Plant>) {
        CoroutineScope(Dispatchers.IO).launch {
            dao?.insertAll(plantList)
            plants.postValue(dao?.getAll())
        }
    }
}

