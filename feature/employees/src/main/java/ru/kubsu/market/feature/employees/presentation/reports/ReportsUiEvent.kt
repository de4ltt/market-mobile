package ru.kubsu.market.feature.employees.presentation.reports

sealed interface ReportsUiEvent {
    data class ShowToast(val message: String) : ReportsUiEvent
}
