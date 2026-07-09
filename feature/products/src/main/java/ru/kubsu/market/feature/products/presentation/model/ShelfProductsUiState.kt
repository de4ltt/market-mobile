package ru.kubsu.market.feature.products.presentation.model

import ru.kubsu.market.core.model.Product

sealed interface ShelfProductsUiState {
    data object Loading : ShelfProductsUiState
    data class Success(val items: List<Product>) : ShelfProductsUiState
    data class Error(val message: String) : ShelfProductsUiState
}
