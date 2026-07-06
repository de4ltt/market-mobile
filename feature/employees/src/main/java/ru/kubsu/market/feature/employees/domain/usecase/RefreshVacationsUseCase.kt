package ru.kubsu.market.feature.employees.domain.usecase

import ru.kubsu.market.feature.employees.domain.EmployeesRepository

class RefreshVacationsUseCase(
    private val repository: EmployeesRepository
) {
    suspend operator fun invoke() = repository.refreshVacations()
}
