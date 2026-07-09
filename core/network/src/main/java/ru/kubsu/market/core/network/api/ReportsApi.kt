package ru.kubsu.market.core.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kubsu.market.core.network.ApiConfig
import ru.kubsu.market.core.network.dto.ConfirmReportRequestDto
import ru.kubsu.market.core.network.dto.PersonnelReportDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportsApi @Inject constructor(
    private val httpClient: HttpClient
) {
    private val baseUrl = ApiConfig.BASE_URL

    suspend fun getReportsCurrentWeek(): List<PersonnelReportDto> {
        return httpClient.get("$baseUrl/reports/current-week").body()
    }

    suspend fun getEmployeeReports(employeeId: Int): List<PersonnelReportDto> {
        return httpClient.get("$baseUrl/reports/employee/$employeeId").body()
    }

    suspend fun updateReport(reportId: Int, request: ConfirmReportRequestDto): PersonnelReportDto {
        return httpClient.post("$baseUrl/reports/$reportId/update") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun confirmCurrentWeek() {
        httpClient.post("$baseUrl/reports/confirm-current-week") {
            contentType(ContentType.Application.Json)
        }
    }
}
