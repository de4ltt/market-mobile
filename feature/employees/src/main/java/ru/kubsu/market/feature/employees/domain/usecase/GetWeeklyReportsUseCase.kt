package ru.kubsu.market.feature.employees.domain.usecase

import ru.kubsu.market.core.model.PersonnelReport
import ru.kubsu.market.feature.employees.domain.ReportsRepository
import javax.inject.Inject

class GetWeeklyReportsUseCase @Inject constructor(
    private val repository: ReportsRepository
) {
    suspend operator fun invoke(): List<PersonnelReport> {
        return repository.getReportsCurrentWeek()
    }
}
