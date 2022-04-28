package com.example.plantreapp.entities
import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*



@Entity
data class PlantIdentity (
        @PrimaryKey(autoGenerate = true) val position: Int,
        @ColumnInfo(name = "pName") val pName: String?,
) {

    override fun hashCode(): Int {
        return Objects.hash(position, pName)
    }
    companion object {
        var itemCallback: DiffUtil.ItemCallback<PlantIdentity> = object : DiffUtil.ItemCallback<PlantIdentity>() {
            override fun areItemsTheSame(oldItem: PlantIdentity, newItem: PlantIdentity): Boolean {
                return oldItem.position == newItem.position
            }

            override fun areContentsTheSame(oldItem: PlantIdentity, newItem: PlantIdentity): Boolean {
                return oldItem == newItem
            }
        }
    }

}