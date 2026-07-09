package ru.kubsu.market.feature.employees.presentation.model

import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Position
import ru.kubsu.market.core.model.Vacation

sealed interface EmployeesScreenState {
    data class Employees(val employees: List<Employee>, val positions: List<Position>) : EmployeesScreenState
    data class Vacations(val vacations: List<Vacation>) : EmployeesScreenState
    data object Loading : EmployeesScreenState
    data class Error(val message: String) : EmployeesScreenState
}
