package com.example.plantreapp.entities

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Plant (
    @PrimaryKey(autoGenerate = true) val uid:Int?,
    @ColumnInfo(name = "sensor_uuid") val sensor_uuid: String?,// Unique ID of sensor attached to
    @ColumnInfo(name = "nickname") val nickname: String?, // A psuedonym for the plant
    @ColumnInfo(name = "name") val name: String?, // common name of plant - rose, pink tulip, climax blueberry, autumncrisp grape
    @ColumnInfo(name = "scientific_name") val scientific_name: String?,
    @ColumnInfo(name = "picture") val picture: String?, // URI to the picture
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "stage") val stage: String?, // seed - seedling - mature
    @ColumnInfo(name = "seed_water_rate") val seed_water_rate: Int?, // How often to water - in days 1-7, generally seedlings need a higher rate
    @ColumnInfo(name = "seedling_water_rate") val seedling_water_rate: Int?, // How often to water - in days 1-7, generally seedlings need a higher rate
    @ColumnInfo(name = "mature_water_rate") val mature_water_rate: Int?, // How often to water - in days 1-7
    @ColumnInfo(name = "min_seed_moisture") val min_seed_moisture: Float?, // minimum moisture level - seed
    @ColumnInfo(name = "max_seed_moisture")  val max_seed_moisture: Float?, // maximum moisture level - seed
    @ColumnInfo(name = "min_seedling_moisture") val min_seedling_moisture: Float?, // minimum moisture level - seedling
    @ColumnInfo(name = "max_seedling_moisture")  val max_seedling_moisture: Float?, // maximum moisture level - seedling
    @ColumnInfo(name = "min_mature_moisture") val min_mature_moisture: Float?, // minimum moisture level - Mature
    @ColumnInfo(name = "max_mature_moisture")  val max_mature_moisture: Float?, // maximum moisture level - Mature
    @ColumnInfo(name = "position") val position: Int?,
    @ColumnInfo(name = "water_running_time") val water_running_time: Int
) {

    override fun hashCode(): Int {
        return Objects.hash(uid, name, description)
    }
    companion object {
        var itemCallback: DiffUtil.ItemCallback<Plant> = object : DiffUtil.ItemCallback<Plant>() {
            override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
                return oldItem == newItem
            }
        }
    }

}
