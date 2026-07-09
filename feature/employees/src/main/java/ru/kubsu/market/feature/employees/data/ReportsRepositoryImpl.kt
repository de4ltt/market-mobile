package ru.kubsu.market.feature.employees.data

import ru.kubsu.market.core.model.ConfirmReportRequest
import ru.kubsu.market.core.model.PersonnelReport
import ru.kubsu.market.core.network.api.ReportsApi
import ru.kubsu.market.core.network.mapper.toDomain
import ru.kubsu.market.core.network.mapper.toDto
import ru.kubsu.market.feature.employees.domain.ReportsRepository
import javax.inject.Inject

class ReportsRepositoryImpl @Inject constructor(
    private val reportsApi: ReportsApi
) : ReportsRepository {

    override suspend fun getReportsCurrentWeek(): List<PersonnelReport> {
        return reportsApi.getReportsCurrentWeek().map { it.toDomain() }
    }

    override suspend fun getEmployeeReports(employeeId: Int): List<PersonnelReport> {
        return reportsApi.getEmployeeReports(employeeId).map { it.toDomain() }
    }

    override suspend fun updateReport(reportId: Int, request: ConfirmReportRequest): PersonnelReport {
        return reportsApi.updateReport(reportId, request.toDto()).toDomain()
    }

    override suspend fun confirmCurrentWeek() {
        reportsApi.confirmCurrentWeek()
    }
}
