package com.example.wardrobegenerator.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.wardrobegenerator.data.local.Converters

@Entity(tableName = "outfits")
@TypeConverters(Converters::class)
data class Outfit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val itemIds: List<Long>,
    val dateCreated: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
