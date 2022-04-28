package com.example.plantreapp.dao

import androidx.room.*
import com.example.plantreapp.entities.Moisture

@Dao
interface MoistureDAO {
    @Query("SELECT * FROM moisture")
    suspend fun getAll(): List<Moisture>

    @Insert
    suspend fun insert(moisture: Moisture)

    @Update
    suspend fun update(moisture: Moisture)

    @Delete
    suspend fun delete(moisture: Moisture)

    @Query("SELECT * FROM moisture WHERE uid = :id")
    suspend fun findById(id: Int): List<Moisture>
}