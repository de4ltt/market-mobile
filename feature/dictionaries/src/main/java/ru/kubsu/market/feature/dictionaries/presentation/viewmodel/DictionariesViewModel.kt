package ru.kubsu.market.feature.dictionaries.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.core.model.IDictionaryFetcher
import ru.kubsu.market.core.model.IDictionaryItem
import ru.kubsu.market.core.model.IItemRepresentable
import ru.kubsu.market.feature.dictionaries.domain.usecase.GetDictionaryItemsUseCase

sealed interface DictionariesUiState {
    data object Idle : DictionariesUiState
    data object Loading : DictionariesUiState
    data class Success(val items: List<IItemRepresentable>, val className: String) : DictionariesUiState
    data class Error(val message: String) : DictionariesUiState
}

class DictionariesViewModel(
    private val getDictionaryItemsUseCase: GetDictionaryItemsUseCase
) : ViewModel(), IDictionaryFetcher {

    private val _uiState = MutableStateFlow<DictionariesUiState>(DictionariesUiState.Idle)
    val uiState: StateFlow<DictionariesUiState> = _uiState.asStateFlow()

    override fun getDictionaryItems(item: IDictionaryItem) {
        _uiState.value = DictionariesUiState.Loading
        viewModelScope.launch {
            try {
                val list = getDictionaryItemsUseCase(item)
                _uiState.value = DictionariesUiState.Success(list, item.className)
            } catch (e: Exception) {
                _uiState.value = DictionariesUiState.Error(e.message ?: "Ошибка загрузки справочника")
            }
        }
    }

    fun reset() {
        _uiState.value = DictionariesUiState.Idle
    }
}

class DictionariesViewModelFactory(
    private val getDictionaryItemsUseCase: GetDictionaryItemsUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DictionariesViewModel::class.java)) {
            return DictionariesViewModel(getDictionaryItemsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
