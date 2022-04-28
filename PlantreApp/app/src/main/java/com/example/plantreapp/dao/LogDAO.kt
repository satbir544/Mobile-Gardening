package com.example.plantreapp.dao

import androidx.room.*
import com.example.plantreapp.entities.Log

@Dao
interface LogDAO {
    @Query("SELECT * FROM log")
    suspend fun getAll(): List<Log>

    @Insert
    suspend fun insert(log: Log)

    @Delete
    suspend fun delete(log: Log)

    @Update
    suspend fun update(log: Log)

    @Query("SELECT * FROM log WHERE name = :name")
    suspend fun findByName(name: String): List<Log>

    @Query("SELECT * FROM log WHERE uid = :id")
    suspend fun findById(id: Int): List<Log>

    @Query("SELECT * FROM log WHERE journal_UID = :journalUID")
    suspend fun findByJournalUID(journalUID: Int): List<Log>
}