package com.example.wardrobegenerator.data.local

import androidx.room.TypeConverter
import com.example.wardrobegenerator.data.model.ClothingCategory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromClothingCategory(category: ClothingCategory): String {
        return category.name
    }

    @TypeConverter
    fun toClothingCategory(categoryString: String): ClothingCategory {
        return ClothingCategory.valueOf(categoryString)
    }

    @TypeConverter
    fun fromLongList(value: List<Long>): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toLongList(value: String): List<Long> {
        val gson = Gson()
        val type = object : TypeToken<List<Long>>() {}.type
        return gson.fromJson(value, type)
    }
}
