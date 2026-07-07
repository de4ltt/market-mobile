package ru.kubsu.market.feature.employees.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kubsu.market.core.model.ConfirmReportRequest
import ru.kubsu.market.core.model.PersonnelReport
import ru.kubsu.market.feature.employees.domain.ReportsRepository
import javax.inject.Inject

class ReportsRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : ReportsRepository {

    private val baseUrl = ru.kubsu.market.core.network.ApiConfig.BASE_URL

    override suspend fun getReportsCurrentWeek(): List<PersonnelReport> {
        return httpClient.get("$baseUrl/reports/current-week").body()
    }

    override suspend fun getEmployeeReports(employeeId: Int): List<PersonnelReport> {
        return httpClient.get("$baseUrl/reports/employee/$employeeId").body()
    }

    override suspend fun updateReport(reportId: Int, request: ConfirmReportRequest): PersonnelReport {
        return httpClient.post("$baseUrl/reports/$reportId/update") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun confirmCurrentWeek() {
        httpClient.post("$baseUrl/reports/confirm-current-week") {
            contentType(ContentType.Application.Json)
        }
    }
}
