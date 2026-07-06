package ru.kubsu.market.feature.employees.domain

import ru.kubsu.market.core.model.ConfirmReportRequest
import ru.kubsu.market.core.model.PersonnelReport

interface ReportsRepository {
    suspend fun getReportsCurrentWeek(): List<PersonnelReport>
    suspend fun getEmployeeReports(employeeId: Int): List<PersonnelReport>
    suspend fun updateReport(reportId: Int, request: ConfirmReportRequest): PersonnelReport
    suspend fun confirmCurrentWeek()
}
