package ru.kubsu.market.feature.employees.domain.usecase

import ru.kubsu.market.core.model.Position
import ru.kubsu.market.feature.employees.domain.EmployeesRepository

import javax.inject.Inject

class GetPositionsUseCase @Inject constructor(
    private val repository: EmployeesRepository
) {
    suspend operator fun invoke(): List<Position> = repository.getPositions()
}
