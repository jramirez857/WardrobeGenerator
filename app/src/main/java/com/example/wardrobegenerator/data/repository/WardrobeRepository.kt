package com.example.wardrobegenerator.data.repository

import android.net.Uri
import com.example.wardrobegenerator.data.local.dao.ClothingItemDao
import com.example.wardrobegenerator.data.local.dao.OutfitDao
import com.example.wardrobegenerator.data.model.ClothingCategory
import com.example.wardrobegenerator.data.model.ClothingItem
import com.example.wardrobegenerator.data.model.Outfit
import com.example.wardrobegenerator.data.storage.ImageStorageManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that manages wardrobe data from local database and image storage.
 * This abstraction makes it easy to add cloud storage in the future.
 */
@Singleton
class WardrobeRepository @Inject constructor(
    private val clothingItemDao: ClothingItemDao,
    private val outfitDao: OutfitDao,
    private val imageStorageManager: ImageStorageManager
) {

    // ========== Clothing Items ==========

    fun getAllClothingItems(): Flow<List<ClothingItem>> {
        return clothingItemDao.getAllItems()
    }

    fun getClothingItemsByCategory(category: ClothingCategory): Flow<List<ClothingItem>> {
        return clothingItemDao.getItemsByCategory(category)
    }

    suspend fun getClothingItemById(id: Long): ClothingItem? {
        return clothingItemDao.getItemById(id)
    }

    suspend fun addClothingItem(
        name: String,
        category: ClothingCategory,
        imageUri: Uri,
        color: String? = null,
        season: String? = null
    ): Long {
        // Save image to storage
        val savedImagePath = imageStorageManager.saveImage(imageUri, category.name)

        // Create and save clothing item
        val item = ClothingItem(
            name = name,
            category = category,
            color = color,
            imageUri = savedImagePath,
            season = season
        )

        return clothingItemDao.insertItem(item)
    }

    suspend fun updateClothingItem(item: ClothingItem) {
        clothingItemDao.updateItem(item)
    }

    suspend fun deleteClothingItem(item: ClothingItem) {
        // Delete image from storage
        imageStorageManager.deleteImage(item.imageUri)

        // Delete from database
        clothingItemDao.deleteItem(item)
    }

    suspend fun getClothingItemCount(): Int {
        return clothingItemDao.getItemCount()
    }

    // ========== Outfits ==========

    fun getAllOutfits(): Flow<List<Outfit>> {
        return outfitDao.getAllOutfits()
    }

    fun getFavoriteOutfits(): Flow<List<Outfit>> {
        return outfitDao.getFavoriteOutfits()
    }

    suspend fun getOutfitById(id: Long): Outfit? {
        return outfitDao.getOutfitById(id)
    }

    suspend fun saveOutfit(name: String, itemIds: List<Long>): Long {
        val outfit = Outfit(
            name = name,
            itemIds = itemIds
        )
        return outfitDao.insertOutfit(outfit)
    }

    suspend fun updateOutfit(outfit: Outfit) {
        outfitDao.updateOutfit(outfit)
    }

    suspend fun deleteOutfit(outfit: Outfit) {
        outfitDao.deleteOutfit(outfit)
    }

    suspend fun toggleOutfitFavorite(outfit: Outfit) {
        val updated = outfit.copy(isFavorite = !outfit.isFavorite)
        outfitDao.updateOutfit(updated)
    }

    // ========== Storage Management ==========

    suspend fun getStorageUsed(): Long {
        return imageStorageManager.getStorageUsed()
    }
}
