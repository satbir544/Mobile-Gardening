package com.example.plantreapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.plantreapp.dao.MoistureDAO
import com.example.plantreapp.db.AppDatabase
import com.example.plantreapp.entities.Moisture
import kotlinx.coroutines.runBlocking

class MoistureRepository(context: Context) {
    private var dao: MoistureDAO? = null
    var moistures: MutableLiveData<List<Moisture>> = MutableLiveData()

    init  {
        val db = AppDatabase.invoke(context)
        dao = db.moistureDao()
        runBlocking {
            moistures.value = dao?.getAll()
        }
    }

    suspend fun getAll() : List<Moisture> {
        val list: List<Moisture> = emptyList()
        return dao?.getAll() ?: list
    }

    suspend fun findById(id: Int) : List<Moisture> {
        val list: List<Moisture> = emptyList()
        return dao?.findById(id) ?: list
    }

    suspend fun insert(moisture: Moisture) {
        runBlocking { dao?.insert(moisture) }
    }

    suspend fun update(moisture: Moisture) {
        runBlocking { dao?.update(moisture) }
    }

    suspend fun delete(moisture: Moisture) {
        runBlocking { dao?.delete(moisture) }
    }

}