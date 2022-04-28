package com.example.plantreapp.dao

import androidx.room.*
import com.example.plantreapp.entities.Plant
import com.example.plantreapp.entities.PlantInfo

@Dao
interface PlantInfoDAO {
    @Query("SELECT * FROM plantInfo")
    suspend fun getAll(): List<PlantInfo>

    @Insert
    suspend fun insert(plant: PlantInfo)

    @Insert
    suspend fun insertAll(plants: List<PlantInfo>)

    @Delete
    suspend fun delete(plant: PlantInfo)

    @Update
    suspend fun update(plant: PlantInfo)

    @Query("SELECT * FROM plantInfo WHERE name = :name OR scientific_name = :name")
    suspend fun findByName(name: String) : List<PlantInfo>

    @Query("SELECT * FROM plantInfo WHERE uid = :id")
    suspend fun findById(id: Int) : List<PlantInfo>

}