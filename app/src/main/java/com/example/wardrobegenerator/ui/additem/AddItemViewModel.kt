package com.example.wardrobegenerator.ui.additem

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobegenerator.data.model.ClothingCategory
import com.example.wardrobegenerator.data.repository.WardrobeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val repository: WardrobeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddItemUiState>(AddItemUiState.Initial)
    val uiState: StateFlow<AddItemUiState> = _uiState.asStateFlow()

    fun saveClothingItem(imageUri: Uri, name: String, category: ClothingCategory, color: String?) {
        viewModelScope.launch {
            _uiState.value = AddItemUiState.Saving
            try {
                repository.addClothingItem(
                    name = name,
                    category = category,
                    imageUri = imageUri,
                    color = color
                )
                _uiState.value = AddItemUiState.Success
            } catch (e: FileNotFoundException) {
                _uiState.value = AddItemUiState.Error("Image file not found")
            } catch (e: IOException) {
                _uiState.value = AddItemUiState.Error("Failed to save image: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = AddItemUiState.Initial
    }
}

sealed class AddItemUiState {
    object Initial : AddItemUiState()
    object Saving : AddItemUiState()
    object Success : AddItemUiState()
    data class Error(val message: String) : AddItemUiState()
}
