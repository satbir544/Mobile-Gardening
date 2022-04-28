package com.example.plantreapp.repository;

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.plantreapp.dao.PlantIdentityDAO
import com.example.plantreapp.db.AppDatabase
import com.example.plantreapp.entities.PlantIdentity
import kotlinx.coroutines.runBlocking

class PlantIdentityRepository(context: Context) {
    private var dao: PlantIdentityDAO? = null
    var plantIDs: MutableLiveData<List<PlantIdentity>> = MutableLiveData()

    init  {
        val db = AppDatabase.invoke(context)
        dao = db.plantIdentityDao()
        runBlocking {
            plantIDs.value = dao?.getAll()
        }
    }

    suspend fun getAll() : List<PlantIdentity> {
        val list: List<PlantIdentity> = emptyList()
        return dao?.getAll() ?: list
    }

    suspend fun update(plantIdentity: PlantIdentity) {
        runBlocking { dao?.update(plantIdentity) }
    }


    suspend fun insert(plantIdentity: PlantIdentity) {
        runBlocking { dao?.insert(plantIdentity) }
    }

    suspend fun delete(plantIdentity: PlantIdentity, plant_uid: Int) {
        runBlocking { dao?.delete(plantIdentity) }
        runBlocking {
            plantIDs.value = dao?.findById(plant_uid)
        }
    }

    suspend fun findByPosition(position: Int) : PlantIdentity? {
        return dao?.findByPositionNumber(position)
    }

}