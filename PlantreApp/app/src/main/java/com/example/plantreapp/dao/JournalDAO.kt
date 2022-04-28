package com.example.plantreapp.dao

import androidx.room.*
import com.example.plantreapp.entities.Journal

@Dao
interface JournalDAO {
    @Query("SELECT * FROM journal")
    suspend fun getAll(): List<Journal>

    @Insert
    suspend fun insert(journal: Journal)

    @Update
    suspend fun update(journal: Journal)

    @Delete
    suspend fun delete(journal: Journal)

    @Query("SELECT * FROM journal WHERE name = :name")
    suspend fun findByName(name: String): List<Journal>

    @Query("SELECT * FROM journal WHERE uid = :id")
    suspend fun findById(id: Int): List<Journal>

    @Query("SELECT * FROM journal WHERE plant_uid = :plant_uid")
    suspend fun findByPlantUID(plant_uid: Int): List<Journal>


}