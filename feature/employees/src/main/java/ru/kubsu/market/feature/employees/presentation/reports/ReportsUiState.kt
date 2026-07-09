package ru.kubsu.market.feature.employees.presentation.reports

import ru.kubsu.market.core.model.PersonnelReport

sealed interface ReportsUiState {
    data object Loading : ReportsUiState
    data class Success(val reports: List<PersonnelReport>) : ReportsUiState
    data class Error(val message: String) : ReportsUiState
}
