package ru.kubsu.market.feature.employees.domain.usecase

import ru.kubsu.market.core.model.PersonnelReport
import ru.kubsu.market.feature.employees.domain.ReportsRepository
import javax.inject.Inject

class GetEmployeeReportsUseCase @Inject constructor(
    private val repository: ReportsRepository
) {
    suspend operator fun invoke(employeeId: Int): List<PersonnelReport> {
        return repository.getEmployeeReports(employeeId)
    }
}
