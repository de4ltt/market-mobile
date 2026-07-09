package ru.kubsu.market.feature.products.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.feature.products.domain.usecase.FormOrderUseCase
import ru.kubsu.market.feature.products.domain.usecase.GetProductPricesUseCase
import ru.kubsu.market.feature.products.domain.usecase.GetProductsUseCase
import ru.kubsu.market.feature.products.presentation.model.ProductsUiEvent
import ru.kubsu.market.feature.products.presentation.model.ProductsUiState

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductPricesUseCase: GetProductPricesUseCase,
    private val formOrderUseCase: FormOrderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductsUiState>(ProductsUiState.Loading)
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ProductsUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ProductsUiEvent> = _uiEvent.receiveAsFlow()

    fun loadProducts() {
        _uiState.value = ProductsUiState.Loading
        viewModelScope.launch {
            try {
                val products = getProductsUseCase()
                val pricesList = getProductPricesUseCase(products.map { it.productId!! })
                val pricesMap = pricesList.associateBy { it.productId }
                _uiState.value = ProductsUiState.Success(products, pricesMap)
            } catch (e: Exception) {
                _uiState.value = ProductsUiState.Error(e.message ?: "Ошибка при загрузке товаров")
                _uiEvent.send(ProductsUiEvent.ShowToast(e.message ?: "Ошибка при загрузке товаров"))
            }
        }
    }

    fun formOrder(employeeId: Int) {
        viewModelScope.launch {
            try {
                val pricesMap = formOrderUseCase(employeeId)
                val currentState = _uiState.value
                if (currentState is ProductsUiState.Success) {
                    _uiState.value = currentState.copy(prices = pricesMap)
                }
                _uiEvent.send(ProductsUiEvent.OrderFormed)
            } catch (e: Exception) {
                _uiEvent.send(ProductsUiEvent.ShowToast(e.message ?: "Ошибка при формировании заказа"))
            }
        }
    }
}
