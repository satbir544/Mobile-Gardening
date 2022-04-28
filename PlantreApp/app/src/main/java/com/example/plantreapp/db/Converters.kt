package com.example.plantreapp.db

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.ByteArrayInputStream

class Converters {
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val type = Types.newParameterizedType(List::class.java, String::class.java)
    private val listStringAdapter = moshi.adapter<List<String>>(type)

    @TypeConverter
    fun stringListToArray(strings: List<String>) : String {
        return listStringAdapter.toJson(strings)
    }

    @TypeConverter
    fun arrayToStringList(value: String): List<String> {
        val emptyList = listOf<String>()
        return listStringAdapter.fromJson(value)?.toList() ?: emptyList
    }
}