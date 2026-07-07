package ru.kubsu.market.feature.employees.domain.usecase

import ru.kubsu.market.feature.employees.domain.EmployeesRepository

import javax.inject.Inject

class DeleteEmployeeUseCase @Inject constructor(
    private val repository: EmployeesRepository
) {
    suspend operator fun invoke(employeeId: Int) {
        repository.deleteEmployee(employeeId)
        repository.refreshEmployees()
    }
}
