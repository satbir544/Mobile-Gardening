package com.example.plantreapp.entities

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class Moisture (
        @PrimaryKey(autoGenerate = true) val uid: Int?,
        @ColumnInfo(name = "percentage") val percentage: Int,
        @ColumnInfo(name = "text") val text: String,
        @ColumnInfo(name = "btnName") val btnName: String,
        @ColumnInfo(name = "plant_uid") val plantUid: Int // The plant that the moisture sensor is connected to
) {
    override fun hashCode(): Int {
        return Objects.hash(uid, percentage, text, btnName, plantUid)
    }
    companion object {
        var itemCallback: DiffUtil.ItemCallback<Moisture> = object : DiffUtil.ItemCallback<Moisture>() {
            override fun areItemsTheSame(oldItem: Moisture, newItem: Moisture): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: Moisture, newItem: Moisture): Boolean {
                return oldItem == newItem
            }
        }
    }
}