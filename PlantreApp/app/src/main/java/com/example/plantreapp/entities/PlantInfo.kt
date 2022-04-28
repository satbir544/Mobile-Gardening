package com.example.plantreapp.entities

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

// Class : PlantInfo
// Desc : emulate data in database, allows users to hold some of the plant information on the phone
@Entity
@Parcelize
data class PlantInfo (
    @PrimaryKey(autoGenerate = true) val uid:Int?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "scientific_name") val scientific_name: String?,
    @ColumnInfo(name = "pictures") val pictures: List<String>?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "stage") val stage: String?, // seed - seedling - mature
    @ColumnInfo(name = "seed_water_rate") val seed_water_rate: Int, // How often to water - in days 1-7, generally seedlings need a higher rate
    @ColumnInfo(name = "seedling_water_rate") val seedling_water_rate: Int, // How often to water - in days 1-7, generally seedlings need a higher rate
    @ColumnInfo(name = "mature_water_rate") val mature_water_rate: Int, // How often to water - in days 1-7
    @ColumnInfo(name = "min_seed_moisture") val min_seed_moisture: Float, // minimum moisture level - seed
    @ColumnInfo(name = "max_seed_moisture")  val max_seed_moisture: Float, // maximum moisture level - seed
    @ColumnInfo(name = "min_seedling_moisture") val min_seedling_moisture: Float, // minimum moisture level - seedling
    @ColumnInfo(name = "max_seedling_moisture")  val max_seedling_moisture: Float, // maximum moisture level - seedling
    @ColumnInfo(name = "min_mature_moisture") val min_mature_moisture: Float, // minimum moisture level - Mature
    @ColumnInfo(name = "max_mature_moisture")  val max_mature_moisture: Float, // maximum moisture level - Mature
    @ColumnInfo(name = "min_harvest_day") val min_harvest_day: Int, // minimum moisture level - Mature
    @ColumnInfo(name = "max_harvest_day")  val max_harvest_day: Int, // maximum moisture level - Mature

) : Parcelable {

    override fun hashCode(): Int {
        return Objects.hash(uid, name, description)
    }
    companion object {
        var itemCallback: DiffUtil.ItemCallback<PlantInfo> = object : DiffUtil.ItemCallback<PlantInfo>() {
            override fun areItemsTheSame(oldItem: PlantInfo, newItem: PlantInfo): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: PlantInfo, newItem: PlantInfo): Boolean {
                return oldItem == newItem
            }
        }
    }

}