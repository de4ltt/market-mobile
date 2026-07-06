package ru.kubsu.market.feature.shift.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.model.WorkDay
import ru.kubsu.market.core.network.ApiConfig
import ru.kubsu.market.feature.shift.domain.ShiftRepository

class ShiftRepositoryImpl(
    private val httpClient: HttpClient
) : ShiftRepository {

    private val baseUrl = ApiConfig.BASE_URL

    override suspend fun getEmployeeProfile(employeeId: Int): Employee {
        return httpClient.get("$baseUrl/employees/$employeeId").body<Employee>()
    }

    override suspend fun getWorkDays(employeeId: Int, startDate: String, endDate: String): List<WorkDay> {
        return httpClient.get("$baseUrl/time-tracking/$employeeId/work-days") {
            parameter("startDate", startDate)
            parameter("endDate", endDate)
        }.body<List<WorkDay>>()
    }

    override suspend fun requestVacation(vacation: Vacation) {
        httpClient.post("$baseUrl/vacations") {
            contentType(ContentType.Application.Json)
            setBody(vacation)
        }
    }

    override suspend fun getVacationDaysAvailable(employeeId: Int): Long {
        return httpClient.get("$baseUrl/vacations/employee/$employeeId/2026/available").body<Long>()
    }

    override suspend fun getLatestVacation(employeeId: Int): Vacation? {
        val list = httpClient.get("$baseUrl/vacations/employee/$employeeId").body<List<Vacation>>()
        return list.lastOrNull()
    }
}
