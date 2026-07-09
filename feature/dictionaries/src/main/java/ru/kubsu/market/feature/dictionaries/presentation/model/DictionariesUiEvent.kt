package ru.kubsu.market.feature.dictionaries.presentation.model

sealed interface DictionariesUiEvent {
    data class ShowToast(val message: String) : DictionariesUiEvent
}
