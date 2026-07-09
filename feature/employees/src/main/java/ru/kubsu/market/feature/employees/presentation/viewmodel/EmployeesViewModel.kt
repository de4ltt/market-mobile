package ru.kubsu.market.feature.employees.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Position
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.feature.employees.domain.usecase.*
import ru.kubsu.market.feature.employees.presentation.model.EmployeesScreenState
import ru.kubsu.market.feature.employees.presentation.model.EmployeesUiEvent
import javax.inject.Inject

@HiltViewModel
class EmployeesViewModel @Inject constructor(
    private val getEmployeesUseCase: GetEmployeesUseCase,
    private val getVacationsUseCase: GetVacationsUseCase,
    private val getPositionsUseCase: GetPositionsUseCase,
    private val addEmployeeUseCase: AddEmployeeUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase,
    private val requestVacationUseCase: RequestVacationUseCase,
    private val respondToVacationUseCase: RespondToVacationUseCase,
    private val refreshEmployeesUseCase: RefreshEmployeesUseCase,
    private val refreshVacationsUseCase: RefreshVacationsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<EmployeesScreenState>(EmployeesScreenState.Loading)
    val state: StateFlow<EmployeesScreenState> = _state.asStateFlow()

    private val _uiEvent = Channel<EmployeesUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private var cachedPositions: List<Position> = emptyList()

    private var isEmployeesMode: Boolean
        get() = savedStateHandle[KEY_IS_EMPLOYEES_MODE] ?: true
        set(value) {
            savedStateHandle[KEY_IS_EMPLOYEES_MODE] = value
        }

    init {
        viewModelScope.launch {
            getEmployeesUseCase().collectLatest { employees ->
                val currentState = _state.value
                if (isEmployeesMode && currentState !is EmployeesScreenState.Loading && currentState !is EmployeesScreenState.Error) {
                    _state.value = EmployeesScreenState.Employees(employees, cachedPositions)
                }
            }
        }

        viewModelScope.launch {
            getVacationsUseCase().collectLatest { vacations ->
                val currentState = _state.value
                if (!isEmployeesMode && currentState !is EmployeesScreenState.Loading && currentState !is EmployeesScreenState.Error) {
                    _state.value = EmployeesScreenState.Vacations(vacations)
                }
            }
        }

        // Первичная загрузка в зависимости от сохраненной вкладки
        if (isEmployeesMode) {
            loadEmployees()
        } else {
            loadVacations()
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
                val errorMessage = e.message ?: "Ошибка загрузки сотрудников"
                _state.value = EmployeesScreenState.Error(errorMessage)
                _uiEvent.send(EmployeesUiEvent.ShowToast(errorMessage))
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
                val errorMessage = e.message ?: "Ошибка загрузки отпусков"
                _state.value = EmployeesScreenState.Error(errorMessage)
                _uiEvent.send(EmployeesUiEvent.ShowToast(errorMessage))
            }
        }
    }

    fun addEmployee(employee: Employee) {
        viewModelScope.launch {
            try {
                addEmployeeUseCase(employee)
            } catch (e: Exception) {
                _uiEvent.send(EmployeesUiEvent.ShowToast(e.message ?: "Ошибка при добавлении сотрудника"))
            }
        }
    }

    fun deleteEmployee(employeeId: Int) {
        viewModelScope.launch {
            try {
                deleteEmployeeUseCase(employeeId)
            } catch (e: Exception) {
                _uiEvent.send(EmployeesUiEvent.ShowToast(e.message ?: "Ошибка при удалении сотрудника"))
            }
        }
    }

    fun requestVacation(vacation: Vacation) {
        viewModelScope.launch {
            try {
                requestVacationUseCase(vacation)
            } catch (e: Exception) {
                _uiEvent.send(EmployeesUiEvent.ShowToast(e.message ?: "Ошибка при запросе отпуска"))
            }
        }
    }

    fun respondToVacation(vacationId: Int, approve: Boolean) {
        viewModelScope.launch {
            try {
                respondToVacationUseCase(vacationId, approve)
            } catch (e: Exception) {
                _uiEvent.send(EmployeesUiEvent.ShowToast(e.message ?: "Ошибка при ответе на отпуск"))
            }
        }
    }

    companion object {
        private const val KEY_IS_EMPLOYEES_MODE = "is_employees_mode"
    }
}
