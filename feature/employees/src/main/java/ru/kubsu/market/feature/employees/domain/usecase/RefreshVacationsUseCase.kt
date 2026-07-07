package ru.kubsu.market.feature.employees.domain.usecase

import ru.kubsu.market.feature.employees.domain.EmployeesRepository

import javax.inject.Inject

class RefreshVacationsUseCase @Inject constructor(
    private val repository: EmployeesRepository
) {
    suspend operator fun invoke() = repository.refreshVacations()
}
