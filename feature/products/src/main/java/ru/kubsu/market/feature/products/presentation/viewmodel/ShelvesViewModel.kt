package ru.kubsu.market.feature.products.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.feature.products.domain.usecase.GetShelvesUseCase
import ru.kubsu.market.feature.products.domain.usecase.GetStorageLocationsUseCase
import ru.kubsu.market.feature.products.presentation.model.ShelvesUiState

@HiltViewModel
class ShelvesViewModel @Inject constructor(
    private val getShelvesUseCase: GetShelvesUseCase,
    private val getStorageLocationsUseCase: GetStorageLocationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShelvesUiState>(ShelvesUiState.Loading)
    val uiState: StateFlow<ShelvesUiState> = _uiState.asStateFlow()

    fun loadShelves(storageLocationId: Int) {
        _uiState.value = ShelvesUiState.Loading
        viewModelScope.launch {
            try {
                val shelves = getShelvesUseCase(storageLocationId)
                val locations = getStorageLocationsUseCase()
                _uiState.value = ShelvesUiState.Success(locations, shelves)
            } catch (e: Exception) {
                _uiState.value = ShelvesUiState.Error(e.message ?: "Ошибка при загрузке полок")
            }
        }
    }
}
