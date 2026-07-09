package ru.kubsu.market.feature.receival.presentation.model

sealed interface ReceivalUiEvent {
    data object ReceivalFinished : ReceivalUiEvent
    data class ShowToast(val message: String) : ReceivalUiEvent
}
