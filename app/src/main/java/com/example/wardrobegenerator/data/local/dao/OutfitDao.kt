package com.example.wardrobegenerator.data.local.dao

import androidx.room.*
import com.example.wardrobegenerator.data.model.Outfit
import kotlinx.coroutines.flow.Flow

@Dao
interface OutfitDao {

    @Query("SELECT * FROM outfits ORDER BY dateCreated DESC")
    fun getAllOutfits(): Flow<List<Outfit>>

    @Query("SELECT * FROM outfits WHERE isFavorite = 1 ORDER BY dateCreated DESC")
    fun getFavoriteOutfits(): Flow<List<Outfit>>

    @Query("SELECT * FROM outfits WHERE id = :id")
    suspend fun getOutfitById(id: Long): Outfit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutfit(outfit: Outfit): Long

    @Update
    suspend fun updateOutfit(outfit: Outfit)

    @Delete
    suspend fun deleteOutfit(outfit: Outfit)

    @Query("DELETE FROM outfits WHERE id = :id")
    suspend fun deleteOutfitById(id: Long)
}
