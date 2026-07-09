package ru.kubsu.market.feature.shift.presentation.model

sealed interface ShiftUiEvent {
    data class ShowToast(val message: String) : ShiftUiEvent
    data object VacationRequestedSuccessfully : ShiftUiEvent
}
