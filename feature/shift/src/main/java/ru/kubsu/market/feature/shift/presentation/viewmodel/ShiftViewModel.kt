package ru.kubsu.market.feature.shift.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.feature.shift.domain.model.ShiftDetails
import ru.kubsu.market.feature.shift.domain.usecase.GetShiftDetailsUseCase
import ru.kubsu.market.feature.shift.domain.usecase.RequestShiftVacationUseCase
import ru.kubsu.market.feature.shift.presentation.model.ShiftUiEvent
import ru.kubsu.market.feature.shift.presentation.model.ShiftUiState
import javax.inject.Inject

@HiltViewModel
class ShiftViewModel @Inject constructor(
    private val getShiftDetailsUseCase: GetShiftDetailsUseCase,
    private val requestShiftVacationUseCase: RequestShiftVacationUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShiftUiState>(ShiftUiState.Loading)
    val uiState: StateFlow<ShiftUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ShiftUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private val currentEmployeeId: Int?
        get() = savedStateHandle[KEY_EMPLOYEE_ID]

    init {
        currentEmployeeId?.let { employeeId ->
            loadProfile(employeeId)
        }
    }

    fun loadProfile(employeeId: Int) {
        savedStateHandle[KEY_EMPLOYEE_ID] = employeeId
        _uiState.value = ShiftUiState.Loading
        viewModelScope.launch {
            try {
                val details = getShiftDetailsUseCase(employeeId)
                _uiState.value = ShiftUiState.Success(details)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Ошибка загрузки личного кабинета"
                _uiState.value = ShiftUiState.Error(errorMessage)
                _uiEvent.send(ShiftUiEvent.ShowToast(errorMessage))
            }
        }
    }

    fun requestVacation(vacation: Vacation) {
        val empId = currentEmployeeId ?: return
        viewModelScope.launch {
            try {
                requestShiftVacationUseCase(vacation)
                _uiEvent.send(ShiftUiEvent.VacationRequestedSuccessfully)
                loadProfile(empId)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Ошибка при запросе отпуска"
                _uiEvent.send(ShiftUiEvent.ShowToast(errorMessage))
            }
        }
    }

    companion object {
        private const val KEY_EMPLOYEE_ID = "current_employee_id"
    }
}
