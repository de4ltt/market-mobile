package ru.kubsu.market.feature.products.presentation.model

sealed interface ProductsUiEvent {
    data object OrderFormed : ProductsUiEvent
    data class ShowToast(val message: String) : ProductsUiEvent
}
