package com.example.wardrobegenerator.data.local.dao

import androidx.room.*
import com.example.wardrobegenerator.data.model.ClothingCategory
import com.example.wardrobegenerator.data.model.ClothingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ClothingItemDao {

    @Query("SELECT * FROM clothing_items ORDER BY createdAt DESC")
    fun getAllItems(): Flow<List<ClothingItem>>

    @Query("SELECT * FROM clothing_items WHERE category = :category ORDER BY createdAt DESC")
    fun getItemsByCategory(category: ClothingCategory): Flow<List<ClothingItem>>

    @Query("SELECT * FROM clothing_items WHERE id = :id")
    suspend fun getItemById(id: Long): ClothingItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ClothingItem): Long

    @Update
    suspend fun updateItem(item: ClothingItem)

    @Delete
    suspend fun deleteItem(item: ClothingItem)

    @Query("DELETE FROM clothing_items WHERE id = :id")
    suspend fun deleteItemById(id: Long)

    @Query("SELECT COUNT(*) FROM clothing_items")
    suspend fun getItemCount(): Int
}
