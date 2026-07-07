package ru.kubsu.market.feature.employees.domain.usecase

import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.feature.employees.domain.EmployeesRepository

import javax.inject.Inject

class RequestVacationUseCase @Inject constructor(
    private val repository: EmployeesRepository
) {
    suspend operator fun invoke(vacation: Vacation) {
        repository.requestVacation(vacation)
        repository.refreshVacations()
    }
}
