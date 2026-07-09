package ru.kubsu.market.feature.dictionaries.presentation.model

import ru.kubsu.market.core.ui.model.UiDisplayable

sealed interface DictionariesUiState {
    data object Idle : DictionariesUiState
    data object Loading : DictionariesUiState
    data class Success(val items: List<UiDisplayable>, val className: String) : DictionariesUiState
    data class Error(val message: String) : DictionariesUiState
}
