package ru.kubsu.market.feature.employees.domain.usecase

import ru.kubsu.market.feature.employees.domain.EmployeesRepository

import javax.inject.Inject

class RespondToVacationUseCase @Inject constructor(
    private val repository: EmployeesRepository
) {
    suspend operator fun invoke(vacationId: Int, approve: Boolean) {
        repository.respondToVacation(vacationId, approve)
        repository.refreshVacations()
    }
}
