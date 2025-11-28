package com.example.wardrobegenerator.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OutfitTest {

    @Test
    fun `outfit created with correct properties`() {
        val itemIds = listOf(1L, 2L, 3L)
        val outfit = Outfit(
            id = 1,
            name = "Casual Friday",
            itemIds = itemIds,
            dateCreated = 1234567890L,
            isFavorite = true
        )

        assertEquals(1, outfit.id)
        assertEquals("Casual Friday", outfit.name)
        assertEquals(itemIds, outfit.itemIds)
        assertEquals(1234567890L, outfit.dateCreated)
        assertTrue(outfit.isFavorite)
    }

    @Test
    fun `outfit defaults to not favorite`() {
        val outfit = Outfit(
            name = "Work Outfit",
            itemIds = listOf(1L, 2L, 3L)
        )

        assertFalse(outfit.isFavorite)
    }

    @Test
    fun `outfit can toggle favorite status`() {
        val outfit = Outfit(
            id = 1,
            name = "Date Night",
            itemIds = listOf(1L, 2L),
            isFavorite = false
        )

        val favorited = outfit.copy(isFavorite = true)
        assertTrue(favorited.isFavorite)

        val unfavorited = favorited.copy(isFavorite = false)
        assertFalse(unfavorited.isFavorite)
    }

    @Test
    fun `outfit can contain multiple items`() {
        val itemIds = listOf(1L, 2L, 3L, 4L, 5L)
        val outfit = Outfit(
            name = "Complete Look",
            itemIds = itemIds
        )

        assertEquals(5, outfit.itemIds.size)
        assertEquals(itemIds, outfit.itemIds)
    }

    @Test
    fun `outfit can be created with empty item list`() {
        val outfit = Outfit(
            name = "Empty Outfit",
            itemIds = emptyList()
        )

        assertTrue(outfit.itemIds.isEmpty())
    }
}
