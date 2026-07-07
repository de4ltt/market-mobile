package ru.kubsu.market.feature.employees.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.feature.employees.domain.EmployeesRepository

import javax.inject.Inject

class GetEmployeesUseCase @Inject constructor(
    private val repository: EmployeesRepository
) {
    operator fun invoke(): Flow<List<Employee>> = repository.getEmployeesFlow()
}
