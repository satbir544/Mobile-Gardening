package com.example.plantreapp.dao

import androidx.room.*
import com.example.plantreapp.entities.Plant
import com.example.plantreapp.entities.Timer

@Dao
interface TimerDAO {
    @Query("SELECT * FROM timer")
    suspend fun getAll(): List<Timer>

    @Insert
    suspend fun insert(timer: Timer)

    @Delete
    suspend fun delete(timer: Timer)

    @Update
    suspend fun update(timer: Timer)

    @Query("SELECT * FROM timer WHERE uid = :id")
    suspend fun findById(id: Int): List<Timer>

    // Change to match relationship - ie find by journal_UID, plant_UID, log_UID
    @Query("SELECT * FROM timer WHERE plant_uid = :plantUID")
    suspend fun findByPlantUID(plantUID: Int): List<Timer>

    @Query("SELECT * FROM timer WHERE name = :name")
    suspend fun findByName(name: String) : List<Timer>
}