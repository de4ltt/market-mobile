package ru.kubsu.market.feature.employees.domain

import kotlinx.coroutines.flow.Flow
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Position
import ru.kubsu.market.core.model.Vacation

interface EmployeesRepository {
    fun getEmployeesFlow(): Flow<List<Employee>>
    fun getVacationsFlow(): Flow<List<Vacation>>
    suspend fun refreshEmployees()
    suspend fun refreshVacations()
    suspend fun getPositions(): List<Position>
    suspend fun addEmployee(employee: Employee)
    suspend fun deleteEmployee(employeeId: Int)
    suspend fun requestVacation(vacation: Vacation)
}
