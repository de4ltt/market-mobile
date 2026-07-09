package ru.kubsu.market.feature.receival.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.core.model.ReceivedProduct
import ru.kubsu.market.feature.receival.domain.usecase.GetReceivedProductsToResolveUseCase
import ru.kubsu.market.feature.receival.domain.usecase.ResolveProductsUseCase
import ru.kubsu.market.feature.receival.presentation.model.ReceivalUiEvent
import ru.kubsu.market.feature.receival.presentation.model.ReceivalUiState
import javax.inject.Inject

@HiltViewModel
class ReceivalViewModel @Inject constructor(
    private val getReceivedProductsToResolveUseCase: GetReceivedProductsToResolveUseCase,
    private val resolveProductsUseCase: ResolveProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReceivalUiState>(ReceivalUiState.Loading)
    val uiState: StateFlow<ReceivalUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ReceivalUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

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
                _uiEvent.send(ReceivalUiEvent.ReceivalFinished)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Ошибка при сохранении результатов приёмки"
                _uiState.value = ReceivalUiState.Error(errorMessage)
                _uiEvent.send(ReceivalUiEvent.ShowToast(errorMessage))
            }
        }
    }
}
