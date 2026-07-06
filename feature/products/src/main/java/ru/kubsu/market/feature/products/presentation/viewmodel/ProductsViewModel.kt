package ru.kubsu.market.feature.products.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.core.model.Product
import ru.kubsu.market.core.model.ProductPrice
import ru.kubsu.market.core.model.Shelf
import ru.kubsu.market.core.model.StorageLocation
import ru.kubsu.market.feature.products.domain.usecase.FormOrderUseCase
import ru.kubsu.market.feature.products.domain.usecase.GetProductPricesUseCase
import ru.kubsu.market.feature.products.domain.usecase.GetProductsForShelfUseCase
import ru.kubsu.market.feature.products.domain.usecase.GetProductsUseCase
import ru.kubsu.market.feature.products.domain.usecase.GetShelvesUseCase
import ru.kubsu.market.feature.products.domain.usecase.GetStorageLocationsUseCase

sealed interface ProductsUiState {
    data object Loading : ProductsUiState
    data class Products(val items: List<Product>, val prices: Map<Int, ProductPrice>) : ProductsUiState
    data class Storage(val items: List<StorageLocation>) : ProductsUiState
    data class Shelves(val storageLocations: List<StorageLocation>, val items: List<Shelf>) : ProductsUiState
    data class ShelfProducts(val items: List<Product>) : ProductsUiState
    data class Error(val message: String) : ProductsUiState
}

class ProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductPricesUseCase: GetProductPricesUseCase,
    private val formOrderUseCase: FormOrderUseCase,
    private val getStorageLocationsUseCase: GetStorageLocationsUseCase,
    private val getShelvesUseCase: GetShelvesUseCase,
    private val getProductsForShelfUseCase: GetProductsForShelfUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductsUiState>(ProductsUiState.Loading)
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    private val _orderFormed = MutableStateFlow(false)
    val orderFormed: StateFlow<Boolean> = _orderFormed.asStateFlow()

    fun loadProducts() {
        _uiState.value = ProductsUiState.Loading
        viewModelScope.launch {
            try {
                val products = getProductsUseCase()
                val pricesList = getProductPricesUseCase(products.map { it.productId!! })
                val pricesMap = pricesList.associateBy { it.productId }
                _uiState.value = ProductsUiState.Products(products, pricesMap)
            } catch (e: Exception) {
                _uiState.value = ProductsUiState.Error(e.message ?: "Ошибка при загрузке товаров")
            }
        }
    }

    fun formOrder(employeeId: Int) {
        viewModelScope.launch {
            try {
                val result = formOrderUseCase(employeeId)
                val pricesMap = result.productPrices.map { orderPrice ->
                    ProductPrice(
                        productId = orderPrice.productId,
                        productName = "",
                        currentPrice = orderPrice.appliedPrice,
                        regularPrice = orderPrice.regularPrice,
                        labelType = orderPrice.priceTagType.name,
                        effectiveDate = java.time.LocalDate.now()
                    )
                }.associateBy { it.productId }
                val currentState = _uiState.value
                if (currentState is ProductsUiState.Products) {
                    _uiState.value = currentState.copy(prices = pricesMap)
                }
                _orderFormed.value = true
            } catch (e: Exception) {
                _uiState.value = ProductsUiState.Error(e.message ?: "Ошибка при формировании заказа")
            }
        }
    }

    fun clearOrderFormed() {
        _orderFormed.value = false
    }

    fun loadStorageLocations() {
        _uiState.value = ProductsUiState.Loading
        viewModelScope.launch {
            try {
                val locations = getStorageLocationsUseCase()
                _uiState.value = ProductsUiState.Storage(locations)
            } catch (e: Exception) {
                _uiState.value = ProductsUiState.Error(e.message ?: "Ошибка при загрузке складов")
            }
        }
    }

    fun loadShelves(storageLocationId: Int) {
        _uiState.value = ProductsUiState.Loading
        viewModelScope.launch {
            try {
                val shelves = getShelvesUseCase(storageLocationId)
                val locations = getStorageLocationsUseCase()
                _uiState.value = ProductsUiState.Shelves(locations, shelves)
            } catch (e: Exception) {
                _uiState.value = ProductsUiState.Error(e.message ?: "Ошибка при загрузке полок")
            }
        }
    }

    fun loadProductsForShelf(shelfId: Int) {
        _uiState.value = ProductsUiState.Loading
        viewModelScope.launch {
            try {
                val products = getProductsForShelfUseCase(shelfId)
                _uiState.value = ProductsUiState.ShelfProducts(products)
            } catch (e: Exception) {
                _uiState.value = ProductsUiState.Error(e.message ?: "Ошибка при загрузке товаров на полке")
            }
        }
    }
}

class ProductsViewModelFactory(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductPricesUseCase: GetProductPricesUseCase,
    private val formOrderUseCase: FormOrderUseCase,
    private val getStorageLocationsUseCase: GetStorageLocationsUseCase,
    private val getShelvesUseCase: GetShelvesUseCase,
    private val getProductsForShelfUseCase: GetProductsForShelfUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsViewModel::class.java)) {
            return ProductsViewModel(
                getProductsUseCase,
                getProductPricesUseCase,
                formOrderUseCase,
                getStorageLocationsUseCase,
                getShelvesUseCase,
                getProductsForShelfUseCase
              ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
