package com.example.plantreapp.entities

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.*


@JsonClass(generateAdapter = true)
@Entity
data class Log (
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "journal_UID") val journalUID: Int,
    @ColumnInfo(name = "date_created") val dateCreated: String,
    @ColumnInfo(name = "description") val description: String, // Small description, display in list ?
    @ColumnInfo(name = "note") val note: String, // Note to be displayed while note is open - more descriptive text
) {
    override fun hashCode(): Int {
        return Objects.hash(uid, name, description)
    }
    companion object {
        var itemCallback: DiffUtil.ItemCallback<Log> = object : DiffUtil.ItemCallback<Log>() {
            override fun areItemsTheSame(oldItem: Log, newItem: Log): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: Log, newItem: Log): Boolean {
                return oldItem == newItem
            }
        }
    }
}