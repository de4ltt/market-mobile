package ru.kubsu.market.feature.employees.domain.usecase

import ru.kubsu.market.core.model.ConfirmReportRequest
import ru.kubsu.market.core.model.PersonnelReport
import ru.kubsu.market.feature.employees.domain.ReportsRepository
import javax.inject.Inject

class UpdateReportUseCase @Inject constructor(
    private val repository: ReportsRepository
) {
    suspend operator fun invoke(reportId: Int, request: ConfirmReportRequest): PersonnelReport {
        return repository.updateReport(reportId, request)
    }
}
