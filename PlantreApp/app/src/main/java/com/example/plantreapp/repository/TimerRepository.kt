package com.example.plantreapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.room.Update
import com.example.plantreapp.dao.LogDAO
import com.example.plantreapp.dao.TimerDAO
import com.example.plantreapp.db.AppDatabase
import com.example.plantreapp.entities.Timer

class TimerRepository(context: Context) {

    private var dao: TimerDAO? = null
    public var timers: LiveData<List<Timer>>? = null

    init  {
        val db = AppDatabase.invoke(context)
        dao = db.timerDao()
        timers = liveData {
            val data = dao?.getAll()
            if (data != null) {
                emit(data)
            }

        }

    }

    suspend fun getAll() : List<Timer> {
        val list: List<Timer> = emptyList()
        return dao?.getAll() ?: list
    }

    suspend fun findById(id: Int) : List<Timer> {
        val list: List<Timer> = emptyList()
        return dao?.findById(id) ?: list
    }

    suspend fun findByPlantUID(id: Int) : List<Timer> {
        val list: List<Timer> = emptyList()
        return dao?.findByPlantUID(id) ?: list
    }

    suspend fun findByName(name: String) : List<Timer> {
        val list: List<Timer> = emptyList()
        return dao?.findByName(name) ?: list
    }

    suspend fun insert(timer: Timer) {
        dao?.insert(timer)
    }

    suspend fun delete(timer: Timer) {
        dao?.delete(timer)
    }

    suspend fun update(timer: Timer) {
        dao?.update(timer)
    }
}