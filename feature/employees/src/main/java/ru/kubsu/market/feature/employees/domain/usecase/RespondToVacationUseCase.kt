package ru.kubsu.market.feature.employees.domain.usecase

import ru.kubsu.market.feature.employees.domain.EmployeesRepository

class RespondToVacationUseCase(
    private val repository: EmployeesRepository
) {
    suspend operator fun invoke(vacationId: Int, approve: Boolean) {
        repository.respondToVacation(vacationId, approve)
        repository.refreshVacations()
    }
}
