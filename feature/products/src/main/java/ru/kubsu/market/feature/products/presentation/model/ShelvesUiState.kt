package ru.kubsu.market.feature.products.presentation.model

import ru.kubsu.market.core.model.Shelf
import ru.kubsu.market.core.model.StorageLocation

sealed interface ShelvesUiState {
    data object Loading : ShelvesUiState
    data class Success(val storageLocations: List<StorageLocation>, val items: List<Shelf>) : ShelvesUiState
    data class Error(val message: String) : ShelvesUiState
}
