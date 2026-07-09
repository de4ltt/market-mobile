package ru.kubsu.market.feature.auth.presentation.model

sealed interface AuthUiEvent {
    data object LoginSuccess : AuthUiEvent
    data class ShowToast(val message: String) : AuthUiEvent
}
