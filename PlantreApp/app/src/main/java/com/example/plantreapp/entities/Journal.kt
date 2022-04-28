package com.example.plantreapp.entities

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class Journal (
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "is_active") val isActive: Boolean, // Is this journal currently active - IE, should we be sending notifications to water
    @ColumnInfo(name = "last_water_date") val lastWaterDate: String, // Date string - last time the plant was watered, used in combination with plant info to plan notification
    @ColumnInfo(name = "plant_uid") val plantUID: Int, // The plant that the journal is connected to
) {
    override fun hashCode(): Int {
        return Objects.hash(uid, name, description)
    }
    companion object {
        var itemCallback: DiffUtil.ItemCallback<Journal> = object : DiffUtil.ItemCallback<Journal>() {
            override fun areItemsTheSame(oldItem: Journal, newItem: Journal): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: Journal, newItem: Journal): Boolean {
                return oldItem == newItem
            }
        }
    }
}