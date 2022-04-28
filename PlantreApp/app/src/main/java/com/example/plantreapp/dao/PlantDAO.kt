package com.example.plantreapp.dao

import androidx.room.*
import com.example.plantreapp.entities.Plant

@Dao
interface PlantDAO {
    @Query("SELECT * FROM plant")
    suspend fun getAll(): List<Plant>

    @Insert
    suspend fun insert(plant: Plant)

    @Insert
    suspend fun insertAll(plants: List<Plant>)

    @Delete
    suspend fun delete(plant: Plant)

    @Update
    suspend fun update(plant: Plant)

    @Query("SELECT * FROM plant WHERE name = :name OR scientific_name = :name")
    suspend fun findByName(name: String) : List<Plant>

    @Query("SELECT * FROM plant WHERE uid = :id")
    suspend fun findById(id: Int) : List<Plant>

    @Query("SELECT * FROM plant WHERE position = :position")
    suspend fun findByPosition(position: Int): Plant

    @Query("UPDATE plant SET position = NULL WHERE position = :position")
    suspend fun clearOldPositions(position: Int)
}