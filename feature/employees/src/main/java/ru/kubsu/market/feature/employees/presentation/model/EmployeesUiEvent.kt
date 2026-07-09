package ru.kubsu.market.feature.employees.presentation.model

sealed interface EmployeesUiEvent {
    data class ShowToast(val message: String) : EmployeesUiEvent
}
