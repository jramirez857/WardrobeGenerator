package com.example.wardrobegenerator.ui.additem

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.wardrobegenerator.data.model.ClothingCategory
import com.example.wardrobegenerator.data.repository.WardrobeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.FileNotFoundException
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class AddItemViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: WardrobeRepository
    private lateinit var viewModel: AddItemViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = AddItemViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Initial`() {
        assertEquals(AddItemUiState.Initial, viewModel.uiState.value)
    }

    @Test
    fun `saveClothingItem success updates state to Success`() = runTest {
        val mockUri = mock<Uri>()
        whenever(repository.addClothingItem(any(), any(), any(), anyOrNull(), anyOrNull())).thenReturn(1L)

        viewModel.saveClothingItem(
            imageUri = mockUri,
            name = "Test Shirt",
            category = ClothingCategory.TOP,
            color = "Blue"
        )

        testScheduler.advanceUntilIdle()

        assertEquals(AddItemUiState.Success, viewModel.uiState.value)
        verify(repository).addClothingItem(
            name = eq("Test Shirt"),
            category = eq(ClothingCategory.TOP),
            imageUri = eq(mockUri),
            color = eq("Blue"),
            season = anyOrNull()
        )
    }

    @Test
    fun `saveClothingItem with FileNotFoundException shows error`() = runTest {
        val mockUri = mock<Uri>()
        whenever(repository.addClothingItem(any(), any(), any(), anyOrNull(), anyOrNull()))
            .thenAnswer { throw FileNotFoundException("File not found") }

        viewModel.saveClothingItem(
            imageUri = mockUri,
            name = "Test Shirt",
            category = ClothingCategory.TOP,
            color = null
        )

        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is AddItemUiState.Error)
        assertEquals("Image file not found", (state as AddItemUiState.Error).message)
    }

    @Test
    fun `saveClothingItem with IOException shows error`() = runTest {
        val mockUri = mock<Uri>()
        whenever(repository.addClothingItem(any(), any(), any(), anyOrNull(), anyOrNull()))
            .thenAnswer { throw IOException("Storage error") }

        viewModel.saveClothingItem(
            imageUri = mockUri,
            name = "Test Pants",
            category = ClothingCategory.BOTTOM,
            color = "Black"
        )

        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is AddItemUiState.Error)
        assertTrue((state as AddItemUiState.Error).message.contains("Failed to save image"))
    }

    @Test
    fun `resetState sets state back to Initial`() = runTest {
        val mockUri = mock<Uri>()
        whenever(repository.addClothingItem(any(), any(), any(), anyOrNull(), anyOrNull())).thenReturn(1L)

        viewModel.saveClothingItem(mockUri, "Test", ClothingCategory.TOP, null)
        testScheduler.advanceUntilIdle()
        assertEquals(AddItemUiState.Success, viewModel.uiState.value)

        viewModel.resetState()
        assertEquals(AddItemUiState.Initial, viewModel.uiState.value)
    }

    @Test
    fun `saveClothingItem sets Saving state before completion`() = runTest {
        val mockUri = mock<Uri>()
        var capturedState: AddItemUiState? = null

        whenever(repository.addClothingItem(any(), any(), any(), anyOrNull(), anyOrNull())).thenAnswer {
            capturedState = viewModel.uiState.value
            1L
        }

        viewModel.saveClothingItem(mockUri, "Test", ClothingCategory.SHOES, null)

        assertEquals(AddItemUiState.Saving, capturedState)
    }
}
