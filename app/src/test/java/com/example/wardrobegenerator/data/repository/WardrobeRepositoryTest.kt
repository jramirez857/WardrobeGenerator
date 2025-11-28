package com.example.wardrobegenerator.data.repository

import android.net.Uri
import com.example.wardrobegenerator.data.local.dao.ClothingItemDao
import com.example.wardrobegenerator.data.local.dao.OutfitDao
import com.example.wardrobegenerator.data.model.ClothingCategory
import com.example.wardrobegenerator.data.model.ClothingItem
import com.example.wardrobegenerator.data.model.Outfit
import com.example.wardrobegenerator.data.storage.ImageStorageManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class WardrobeRepositoryTest {

    private lateinit var clothingItemDao: ClothingItemDao
    private lateinit var outfitDao: OutfitDao
    private lateinit var imageStorageManager: ImageStorageManager
    private lateinit var repository: WardrobeRepository

    @Before
    fun setup() {
        clothingItemDao = mock()
        outfitDao = mock()
        imageStorageManager = mock()
        repository = WardrobeRepository(clothingItemDao, outfitDao, imageStorageManager)
    }

    // ========== Clothing Item Tests ==========

    @Test
    fun `getAllClothingItems returns items from dao`() = runBlocking {
        val items = listOf(
            ClothingItem(id = 1, name = "Shirt", category = ClothingCategory.TOP, imageUri = "/path1"),
            ClothingItem(id = 2, name = "Pants", category = ClothingCategory.BOTTOM, imageUri = "/path2")
        )
        whenever(clothingItemDao.getAllItems()).thenReturn(flowOf(items))

        val result = repository.getAllClothingItems().first()

        assertEquals(2, result.size)
        assertEquals(items, result)
    }

    @Test
    fun `getClothingItemsByCategory filters correctly`() = runBlocking {
        val topItems = listOf(
            ClothingItem(id = 1, name = "Shirt", category = ClothingCategory.TOP, imageUri = "/path1")
        )
        whenever(clothingItemDao.getItemsByCategory(ClothingCategory.TOP)).thenReturn(flowOf(topItems))

        val result = repository.getClothingItemsByCategory(ClothingCategory.TOP).first()

        assertEquals(1, result.size)
        assertEquals(ClothingCategory.TOP, result[0].category)
    }

    @Test
    fun `addClothingItem saves image and inserts into database`() = runBlocking {
        val mockUri = mock<Uri>()
        val savedPath = "/saved/path/image.jpg"
        whenever(imageStorageManager.saveImage(any(), any())).thenReturn(savedPath)
        whenever(clothingItemDao.insertItem(any())).thenReturn(1L)

        val itemId = repository.addClothingItem(
            name = "Test Shirt",
            category = ClothingCategory.TOP,
            imageUri = mockUri,
            color = "Blue"
        )

        assertEquals(1L, itemId)
        verify(imageStorageManager).saveImage(eq(mockUri), eq(ClothingCategory.TOP.name))
        verify(clothingItemDao).insertItem(any())
    }

    @Test
    fun `deleteClothingItem removes image and database entry`() = runBlocking {
        val item = ClothingItem(
            id = 1,
            name = "Old Shirt",
            category = ClothingCategory.TOP,
            imageUri = "/path/to/image.jpg"
        )
        whenever(imageStorageManager.deleteImage(any())).thenReturn(true)

        repository.deleteClothingItem(item)

        verify(imageStorageManager).deleteImage(item.imageUri)
        verify(clothingItemDao).deleteItem(item)
    }

    @Test
    fun `getClothingItemById returns item when exists`() = runBlocking {
        val item = ClothingItem(id = 1, name = "Shirt", category = ClothingCategory.TOP, imageUri = "/path")
        whenever(clothingItemDao.getItemById(1L)).thenReturn(item)

        val result = repository.getClothingItemById(1L)

        assertNotNull(result)
        assertEquals(item, result)
    }

    @Test
    fun `getClothingItemById returns null when not exists`() = runBlocking {
        whenever(clothingItemDao.getItemById(999L)).thenReturn(null)

        val result = repository.getClothingItemById(999L)

        assertNull(result)
    }

    // ========== Outfit Tests ==========

    @Test
    fun `getAllOutfits returns outfits from dao`() = runBlocking {
        val outfits = listOf(
            Outfit(id = 1, name = "Casual", itemIds = listOf(1L, 2L)),
            Outfit(id = 2, name = "Formal", itemIds = listOf(3L, 4L))
        )
        whenever(outfitDao.getAllOutfits()).thenReturn(flowOf(outfits))

        val result = repository.getAllOutfits().first()

        assertEquals(2, result.size)
        assertEquals(outfits, result)
    }

    @Test
    fun `saveOutfit creates new outfit`() = runBlocking {
        val itemIds = listOf(1L, 2L, 3L)
        whenever(outfitDao.insertOutfit(any())).thenReturn(1L)

        val outfitId = repository.saveOutfit("Summer Look", itemIds)

        assertEquals(1L, outfitId)
        verify(outfitDao).insertOutfit(any())
    }

    @Test
    fun `toggleOutfitFavorite changes favorite status`() = runBlocking {
        val outfit = Outfit(id = 1, name = "Test", itemIds = listOf(1L), isFavorite = false)

        repository.toggleOutfitFavorite(outfit)

        verify(outfitDao).updateOutfit(any())
    }

    @Test
    fun `getFavoriteOutfits returns only favorites`() = runBlocking {
        val favorites = listOf(
            Outfit(id = 1, name = "Favorite", itemIds = listOf(1L), isFavorite = true)
        )
        whenever(outfitDao.getFavoriteOutfits()).thenReturn(flowOf(favorites))

        val result = repository.getFavoriteOutfits().first()

        assertEquals(1, result.size)
        assertEquals(true, result[0].isFavorite)
    }

    // ========== Storage Tests ==========

    @Test
    fun `getStorageUsed returns correct size`() = runBlocking {
        val expectedSize = 1024L * 1024L * 5L // 5 MB
        whenever(imageStorageManager.getStorageUsed()).thenReturn(expectedSize)

        val result = repository.getStorageUsed()

        assertEquals(expectedSize, result)
    }
}
