package ru.kubsu.market.feature.employees.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.feature.employees.domain.EmployeesRepository

import javax.inject.Inject

class GetVacationsUseCase @Inject constructor(
    private val repository: EmployeesRepository
) {
    operator fun invoke(): Flow<List<Vacation>> = repository.getVacationsFlow()
}
