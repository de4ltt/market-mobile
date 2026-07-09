package ru.kubsu.market.feature.shift.presentation.model

import ru.kubsu.market.feature.shift.domain.model.ShiftDetails

sealed interface ShiftUiState {
    data object Loading : ShiftUiState
    data class Success(val details: ShiftDetails) : ShiftUiState
    data class Error(val message: String) : ShiftUiState
}
