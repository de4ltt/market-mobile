package ru.kubsu.market.feature.products.presentation.model

import ru.kubsu.market.core.model.StorageLocation

sealed interface StorageUiState {
    data object Loading : StorageUiState
    data class Success(val items: List<StorageLocation>) : StorageUiState
    data class Error(val message: String) : StorageUiState
}
