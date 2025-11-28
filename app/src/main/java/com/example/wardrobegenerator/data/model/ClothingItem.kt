package com.example.wardrobegenerator.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clothing_items")
data class ClothingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: ClothingCategory,
    val color: String? = null,
    val imageUri: String,
    val season: String? = null,
    val lastWorn: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
