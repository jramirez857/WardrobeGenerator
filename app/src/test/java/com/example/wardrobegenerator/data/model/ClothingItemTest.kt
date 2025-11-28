package com.example.wardrobegenerator.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ClothingItemTest {

    @Test
    fun `clothing item created with correct properties`() {
        val item = ClothingItem(
            id = 1,
            name = "Blue Jeans",
            category = ClothingCategory.BOTTOM,
            color = "Blue",
            imageUri = "/path/to/image.jpg",
            season = "All",
            lastWorn = 1234567890L,
            createdAt = 1234567890L
        )

        assertEquals(1, item.id)
        assertEquals("Blue Jeans", item.name)
        assertEquals(ClothingCategory.BOTTOM, item.category)
        assertEquals("Blue", item.color)
        assertEquals("/path/to/image.jpg", item.imageUri)
        assertEquals("All", item.season)
        assertEquals(1234567890L, item.lastWorn)
        assertEquals(1234567890L, item.createdAt)
    }

    @Test
    fun `clothing item can be created with minimal properties`() {
        val item = ClothingItem(
            name = "T-Shirt",
            category = ClothingCategory.TOP,
            imageUri = "/path/to/image.jpg"
        )

        assertEquals(0, item.id) // Default auto-generated
        assertEquals("T-Shirt", item.name)
        assertEquals(ClothingCategory.TOP, item.category)
        assertEquals(null, item.color)
        assertEquals("/path/to/image.jpg", item.imageUri)
        assertEquals(null, item.season)
        assertEquals(null, item.lastWorn)
    }

    @Test
    fun `clothing item copy works correctly`() {
        val original = ClothingItem(
            id = 1,
            name = "Sneakers",
            category = ClothingCategory.SHOES,
            imageUri = "/path/to/image.jpg"
        )

        val copy = original.copy(name = "Running Shoes")

        assertEquals("Running Shoes", copy.name)
        assertEquals(original.id, copy.id)
        assertEquals(original.category, copy.category)
        assertEquals(original.imageUri, copy.imageUri)
    }

    @Test
    fun `clothing items with same data are equal`() {
        val item1 = ClothingItem(
            id = 1,
            name = "Jacket",
            category = ClothingCategory.OUTERWEAR,
            imageUri = "/path/to/image.jpg",
            createdAt = 1000L
        )

        val item2 = ClothingItem(
            id = 1,
            name = "Jacket",
            category = ClothingCategory.OUTERWEAR,
            imageUri = "/path/to/image.jpg",
            createdAt = 1000L
        )

        assertEquals(item1, item2)
    }

    @Test
    fun `clothing items with different data are not equal`() {
        val item1 = ClothingItem(
            id = 1,
            name = "Jacket",
            category = ClothingCategory.OUTERWEAR,
            imageUri = "/path/to/image.jpg"
        )

        val item2 = ClothingItem(
            id = 2,
            name = "Jacket",
            category = ClothingCategory.OUTERWEAR,
            imageUri = "/path/to/image.jpg"
        )

        assertNotEquals(item1, item2)
    }
}
