package ru.kubsu.market.feature.products.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.feature.products.domain.usecase.GetProductsForShelfUseCase
import ru.kubsu.market.feature.products.presentation.model.ShelfProductsUiState

@HiltViewModel
class ShelfProductsViewModel @Inject constructor(
    private val getProductsForShelfUseCase: GetProductsForShelfUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShelfProductsUiState>(ShelfProductsUiState.Loading)
    val uiState: StateFlow<ShelfProductsUiState> = _uiState.asStateFlow()

    fun loadProductsForShelf(shelfId: Int) {
        _uiState.value = ShelfProductsUiState.Loading
        viewModelScope.launch {
            try {
                val products = getProductsForShelfUseCase(shelfId)
                _uiState.value = ShelfProductsUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = ShelfProductsUiState.Error(e.message ?: "Ошибка при загрузке товаров на полке")
            }
        }
    }
}
