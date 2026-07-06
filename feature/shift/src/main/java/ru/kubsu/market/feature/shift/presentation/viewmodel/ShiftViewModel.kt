package ru.kubsu.market.feature.shift.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.feature.shift.domain.model.ShiftDetails
import ru.kubsu.market.feature.shift.domain.usecase.GetShiftDetailsUseCase
import ru.kubsu.market.feature.shift.domain.usecase.RequestShiftVacationUseCase

sealed interface ShiftUiState {
    data object Loading : ShiftUiState
    data class Success(val details: ShiftDetails) : ShiftUiState
    data class Error(val message: String) : ShiftUiState
}

@HiltViewModel
class ShiftViewModel @Inject constructor(
    private val getShiftDetailsUseCase: GetShiftDetailsUseCase,
    private val requestShiftVacationUseCase: RequestShiftVacationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShiftUiState>(ShiftUiState.Loading)
    val uiState: StateFlow<ShiftUiState> = _uiState.asStateFlow()

    private var currentEmployeeId: Int? = null

    fun loadProfile(employeeId: Int) {
        currentEmployeeId = employeeId
        _uiState.value = ShiftUiState.Loading
        viewModelScope.launch {
            try {
                val details = getShiftDetailsUseCase(employeeId)
                _uiState.value = ShiftUiState.Success(details)
            } catch (e: Exception) {
                _uiState.value = ShiftUiState.Error(e.message ?: "Ошибка загрузки личного кабинета")
            }
        }
    }

    fun requestVacation(vacation: Vacation) {
        val empId = currentEmployeeId ?: return
        viewModelScope.launch {
            try {
                requestShiftVacationUseCase(vacation)
                loadProfile(empId)
            } catch (e: Exception) {
                _uiState.value = ShiftUiState.Error(e.message ?: "Ошибка при запросе отпуска")
            }
        }
    }
}

