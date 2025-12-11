package com.colesattac.cityguide.ui

import androidx.lifecycle.ViewModel
import com.colesattac.cityguide.data.Place
import com.colesattac.cityguide.data.PlacesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Класс для хранения состояния UI
data class AppUiState(
    val categories: List<String> = emptyList(),
    val currentCategoryPlaces: List<Place> = emptyList(),
    val currentSelectedPlace: Place? = null,
    val currentCategory: String = ""
)

// ViewModel для управления логикой и состоянием
class PlacesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        // Загружаем категории при инициализации
        val categories = PlacesRepository.getCategories()
        _uiState.value = AppUiState(categories = categories)
    }

    // Обновляет список мест для выбранной категории
    fun updateCurrentCategory(category: String) {
        val places = PlacesRepository.getPlacesByCategory(category)
        _uiState.update { currentState ->
            currentState.copy(
                currentCategory = category,
                currentCategoryPlaces = places
            )
        }
    }

    // Обновляет информацию о выбранном месте для детального экрана
    fun updateCurrentPlace(placeId: Int) {
        val place = PlacesRepository.getPlaceById(placeId)
        _uiState.update { currentState ->
            currentState.copy(
                currentSelectedPlace = place
            )
        }
    }
}
