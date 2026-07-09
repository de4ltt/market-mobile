package ru.kubsu.market.feature.products.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.feature.products.domain.usecase.GetStorageLocationsUseCase
import ru.kubsu.market.feature.products.presentation.model.StorageUiState

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val getStorageLocationsUseCase: GetStorageLocationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<StorageUiState>(StorageUiState.Loading)
    val uiState: StateFlow<StorageUiState> = _uiState.asStateFlow()

    fun loadStorageLocations() {
        _uiState.value = StorageUiState.Loading
        viewModelScope.launch {
            try {
                val locations = getStorageLocationsUseCase()
                _uiState.value = StorageUiState.Success(locations)
            } catch (e: Exception) {
                _uiState.value = StorageUiState.Error(e.message ?: "Ошибка при загрузке складов")
            }
        }
    }
}
