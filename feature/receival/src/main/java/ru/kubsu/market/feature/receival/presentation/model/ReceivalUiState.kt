package ru.kubsu.market.feature.receival.presentation.model

import ru.kubsu.market.core.model.ReceivedProduct

sealed interface ReceivalUiState {
    data object Loading : ReceivalUiState
    data class Success(val toResolveList: List<ReceivedProduct>) : ReceivalUiState
    data class Error(val message: String) : ReceivalUiState
}
