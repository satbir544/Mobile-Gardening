package com.example.plantreapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.plantreapp.dao.LogDAO
import com.example.plantreapp.db.AppDatabase
import com.example.plantreapp.entities.Journal
import com.example.plantreapp.entities.Log
import kotlinx.coroutines.runBlocking

class LogRepository(context: Context) {
    private var dao: LogDAO? = null
    var logs: MutableLiveData<List<Log>> = MutableLiveData()

    init  {
        val db = AppDatabase.invoke(context)
        dao = db.logDao()
        runBlocking {
            logs.value = dao?.getAll()
        }
    }

    suspend fun getAll() : List<Log> {
        val list: List<Log> = emptyList()
        return dao?.getAll() ?: list
    }

    suspend fun findByName(name: String) : List<Log> {
        val list: List<Log> = emptyList()
        return dao?.findByName(name) ?: list
    }

    suspend fun findById(id: Int) : List<Log> {
        val list: List<Log> = emptyList()
        return dao?.findById(id) ?: list
    }

    suspend fun findByJournalUID(journal_uid: Int) : List<Log> {
        val list: List<Log> = emptyList()
        runBlocking {
            logs.value = dao?.findByJournalUID(journal_uid)
        }
        return dao?.findByJournalUID(journal_uid) ?: list
    }

    suspend fun insert(log: Log, journal_uid: Int) {
        runBlocking { dao?.insert(log) }
        runBlocking {
            logs.value = dao?.findByJournalUID(journal_uid)
        }
    }

    suspend fun delete(log: Log, journal_uid: Int) {
        runBlocking { dao?.delete(log) }
        runBlocking {
            logs.value = dao?.findByJournalUID(journal_uid)
        }
    }
}