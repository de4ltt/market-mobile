package ru.kubsu.market.feature.employees.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.core.model.ConfirmReportRequest
import ru.kubsu.market.core.model.PersonnelReport
import ru.kubsu.market.feature.employees.domain.usecase.ConfirmWeeklyReportsUseCase
import ru.kubsu.market.feature.employees.domain.usecase.GetEmployeeReportsUseCase
import ru.kubsu.market.feature.employees.domain.usecase.GetWeeklyReportsUseCase
import ru.kubsu.market.feature.employees.domain.usecase.UpdateReportUseCase
import javax.inject.Inject

sealed interface ReportsUiState {
    data object Loading : ReportsUiState
    data class Success(val reports: List<PersonnelReport>) : ReportsUiState
    data class Error(val message: String) : ReportsUiState
}

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val getWeeklyReportsUseCase: GetWeeklyReportsUseCase,
    private val getEmployeeReportsUseCase: GetEmployeeReportsUseCase,
    private val updateReportUseCase: UpdateReportUseCase,
    private val confirmWeeklyReportsUseCase: ConfirmWeeklyReportsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ReportsUiState>(ReportsUiState.Loading)
    val state: StateFlow<ReportsUiState> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var currentEmployeeId: Int? = null

    fun loadReports() {
        currentEmployeeId = null
        _state.value = ReportsUiState.Loading
        viewModelScope.launch {
            try {
                val reports = getWeeklyReportsUseCase()
                _state.value = ReportsUiState.Success(reports)
            } catch (e: Exception) {
                _state.value = ReportsUiState.Error(e.message ?: "Ошибка при загрузке отчетов")
            }
        }
    }

    fun loadEmployeeReports(employeeId: Int) {
        currentEmployeeId = employeeId
        _state.value = ReportsUiState.Loading
        viewModelScope.launch {
            try {
                val reports = getEmployeeReportsUseCase(employeeId)
                _state.value = ReportsUiState.Success(reports)
            } catch (e: Exception) {
                _state.value = ReportsUiState.Error(e.message ?: "Ошибка при загрузке отчетов сотрудника")
            }
        }
    }

    fun updateReport(reportId: Int, request: ConfirmReportRequest) {
        val currentState = _state.value
        if (currentState is ReportsUiState.Success) {
            viewModelScope.launch {
                try {
                    val updated = updateReportUseCase(reportId, request)
                    val newReports = currentState.reports.map {
                        if (it.personnelReportId == reportId) updated else it
                    }
                    _state.value = ReportsUiState.Success(newReports)
                } catch (e: Exception) {
                    _error.value = e.message ?: "Ошибка при обновлении отчета"
                }
            }
        }
    }

    fun confirmWeeklyReports() {
        val currentState = _state.value
        if (currentState is ReportsUiState.Success) {
            viewModelScope.launch {
                try {
                    confirmWeeklyReportsUseCase()
                    // Reload
                    if (currentEmployeeId != null) {
                        loadEmployeeReports(currentEmployeeId!!)
                    } else {
                        loadReports()
                    }
                } catch (e: Exception) {
                    _error.value = e.message ?: "Ошибка при подтверждении отчетов"
                }
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
