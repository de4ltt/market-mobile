package ru.kubsu.market.feature.employees.domain.usecase

import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.feature.employees.domain.EmployeesRepository

import javax.inject.Inject

class AddEmployeeUseCase @Inject constructor(
    private val repository: EmployeesRepository
) {
    suspend operator fun invoke(employee: Employee) {
        repository.addEmployee(employee)
        repository.refreshEmployees()
    }
}
