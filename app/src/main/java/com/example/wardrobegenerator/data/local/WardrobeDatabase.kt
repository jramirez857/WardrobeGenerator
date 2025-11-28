package com.example.wardrobegenerator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wardrobegenerator.data.local.dao.ClothingItemDao
import com.example.wardrobegenerator.data.local.dao.OutfitDao
import com.example.wardrobegenerator.data.model.ClothingItem
import com.example.wardrobegenerator.data.model.Outfit

@Database(
    entities = [ClothingItem::class, Outfit::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WardrobeDatabase : RoomDatabase() {
    abstract fun clothingItemDao(): ClothingItemDao
    abstract fun outfitDao(): OutfitDao

    companion object {
        const val DATABASE_NAME = "wardrobe_database"
    }
}
