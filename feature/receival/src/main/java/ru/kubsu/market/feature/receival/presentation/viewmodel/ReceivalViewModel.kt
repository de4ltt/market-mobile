package ru.kubsu.market.feature.receival.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.core.model.ReceivedProduct
import ru.kubsu.market.feature.receival.domain.usecase.GetReceivedProductsToResolveUseCase
import ru.kubsu.market.feature.receival.domain.usecase.ResolveProductsUseCase

sealed interface ReceivalUiState {
    data object Loading : ReceivalUiState
    data class Success(val toResolveList: List<ReceivedProduct>) : ReceivalUiState
    data class Error(val message: String) : ReceivalUiState
    data object Finished : ReceivalUiState
}

@HiltViewModel
class ReceivalViewModel @Inject constructor(
    private val getReceivedProductsToResolveUseCase: GetReceivedProductsToResolveUseCase,
    private val resolveProductsUseCase: ResolveProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReceivalUiState>(ReceivalUiState.Loading)
    val uiState: StateFlow<ReceivalUiState> = _uiState.asStateFlow()

    fun loadProducts() {
        _uiState.value = ReceivalUiState.Loading
        viewModelScope.launch {
            try {
                val products = getReceivedProductsToResolveUseCase()
                _uiState.value = ReceivalUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = ReceivalUiState.Error(e.message ?: "Ошибка при получении списка товаров")
            }
        }
    }

    fun resolveProducts(
        acceptedProducts: List<ReceivedProduct>,
        refusedProducts: List<ReceivedProduct>,
        employeeId: Int
    ) {
        _uiState.value = ReceivalUiState.Loading
        viewModelScope.launch {
            try {
                resolveProductsUseCase(acceptedProducts, refusedProducts, employeeId)
                _uiState.value = ReceivalUiState.Finished
            } catch (e: Exception) {
                _uiState.value = ReceivalUiState.Error(e.message ?: "Ошибка при сохранении результатов приёмки")
            }
        }
    }
}

