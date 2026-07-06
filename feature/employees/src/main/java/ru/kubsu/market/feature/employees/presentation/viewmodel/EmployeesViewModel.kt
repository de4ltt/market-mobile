package ru.kubsu.market.feature.employees.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Position
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.feature.employees.EmployeesScreenState
import ru.kubsu.market.feature.employees.domain.usecase.*

class EmployeesViewModel(
    private val getEmployeesUseCase: GetEmployeesUseCase,
    private val getVacationsUseCase: GetVacationsUseCase,
    private val getPositionsUseCase: GetPositionsUseCase,
    private val addEmployeeUseCase: AddEmployeeUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase,
    private val requestVacationUseCase: RequestVacationUseCase,
    private val respondToVacationUseCase: RespondToVacationUseCase,
    private val refreshEmployeesUseCase: RefreshEmployeesUseCase,
    private val refreshVacationsUseCase: RefreshVacationsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<EmployeesScreenState>(EmployeesScreenState.Loading)
    val state: StateFlow<EmployeesScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var cachedPositions: List<Position> = emptyList()
    private var isEmployeesMode = true

    init {
        viewModelScope.launch {
            getEmployeesUseCase().collectLatest { employees ->
                if (isEmployeesMode && _state.value !is EmployeesScreenState.Loading) {
                    _state.value = EmployeesScreenState.Employees(employees, cachedPositions)
                }
            }
        }

        viewModelScope.launch {
            getVacationsUseCase().collectLatest { vacations ->
                if (!isEmployeesMode && _state.value !is EmployeesScreenState.Loading) {
                    _state.value = EmployeesScreenState.Vacations(vacations)
                }
            }
        }
    }

    fun loadEmployees() {
        isEmployeesMode = true
        _state.value = EmployeesScreenState.Loading
        viewModelScope.launch {
            try {
                cachedPositions = getPositionsUseCase()
                refreshEmployeesUseCase()
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка загрузки сотрудников"
            }
        }
    }

    fun loadVacations() {
        isEmployeesMode = false
        _state.value = EmployeesScreenState.Loading
        viewModelScope.launch {
            try {
                refreshVacationsUseCase()
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка загрузки отпусков"
            }
        }
    }

    fun addEmployee(employee: Employee) {
        viewModelScope.launch {
            try {
                addEmployeeUseCase(employee)
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка при добавлении сотрудника"
            }
        }
    }

    fun deleteEmployee(employeeId: Int) {
        viewModelScope.launch {
            try {
                deleteEmployeeUseCase(employeeId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка при удалении сотрудника"
            }
        }
    }

    fun requestVacation(vacation: Vacation) {
        viewModelScope.launch {
            try {
                requestVacationUseCase(vacation)
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка при запросе отпуска"
            }
        }
    }

    fun respondToVacation(vacationId: Int, approve: Boolean) {
        viewModelScope.launch {
            try {
                respondToVacationUseCase(vacationId, approve)
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка при ответе на отпуск"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}

class EmployeesViewModelFactory(
    private val getEmployeesUseCase: GetEmployeesUseCase,
    private val getVacationsUseCase: GetVacationsUseCase,
    private val getPositionsUseCase: GetPositionsUseCase,
    private val addEmployeeUseCase: AddEmployeeUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase,
    private val requestVacationUseCase: RequestVacationUseCase,
    private val respondToVacationUseCase: RespondToVacationUseCase,
    private val refreshEmployeesUseCase: RefreshEmployeesUseCase,
    private val refreshVacationsUseCase: RefreshVacationsUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmployeesViewModel::class.java)) {
            return EmployeesViewModel(
                getEmployeesUseCase,
                getVacationsUseCase,
                getPositionsUseCase,
                addEmployeeUseCase,
                deleteEmployeeUseCase,
                requestVacationUseCase,
                respondToVacationUseCase,
                refreshEmployeesUseCase,
                refreshVacationsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
