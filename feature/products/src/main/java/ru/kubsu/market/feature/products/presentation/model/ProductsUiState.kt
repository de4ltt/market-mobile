package ru.kubsu.market.feature.products.presentation.model

import ru.kubsu.market.core.model.Product
import ru.kubsu.market.core.model.ProductPrice

sealed interface ProductsUiState {
    data object Loading : ProductsUiState
    data class Success(val items: List<Product>, val prices: Map<Int, ProductPrice>) : ProductsUiState
    data class Error(val message: String) : ProductsUiState
}
