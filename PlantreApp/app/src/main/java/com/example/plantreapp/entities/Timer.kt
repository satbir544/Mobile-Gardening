package com.example.plantreapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Timer (
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "plant_uid") val plantUID: Int, // Change this to change the relationship - should it be on a journal, log, plant basis?
    @ColumnInfo(name = "stage") val stage: String?, // seed - seedling - mature
    @ColumnInfo(name = "seedling_water_rate") val seedling_water_rate: Float, // How often to water - in days 1-7, generally seedlings need a higher rate
    @ColumnInfo(name = "seed_water_rate") val seed_water_rate: Float, // How often to water - in days 1-7, generally seedlings need a higher rate
    @ColumnInfo(name = "mature_water_rate") val mature_water_rate: Float, // How often to water - in days 1-7
    @ColumnInfo(name = "last_notified") val lastNotified: String,
    @ColumnInfo(name = "current_Time") val currentTime: String,
    @ColumnInfo(name = "date_created") val dateCreated: String,
)