package ru.kubsu.market.feature.employees.domain.usecase

import ru.kubsu.market.feature.employees.domain.ReportsRepository
import javax.inject.Inject

class ConfirmWeeklyReportsUseCase @Inject constructor(
    private val repository: ReportsRepository
) {
    suspend operator fun invoke() {
        repository.confirmCurrentWeek()
    }
}
