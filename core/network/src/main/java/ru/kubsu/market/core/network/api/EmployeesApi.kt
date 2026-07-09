package ru.kubsu.market.core.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kubsu.market.core.network.ApiConfig
import ru.kubsu.market.core.network.dto.EmployeeDto
import ru.kubsu.market.core.network.dto.PositionDto
import ru.kubsu.market.core.network.dto.VacationDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmployeesApi @Inject constructor(
    private val httpClient: HttpClient
) {
    private val baseUrl = ApiConfig.BASE_URL

    suspend fun getEmployees(): List<EmployeeDto> {
        return httpClient.get("$baseUrl/employees").body()
    }

    suspend fun getVacations(): List<VacationDto> {
        return httpClient.get("$baseUrl/vacations").body()
    }

    suspend fun getPositions(): List<PositionDto> {
        return httpClient.get("$baseUrl/positions").body()
    }

    suspend fun addEmployee(employee: EmployeeDto): EmployeeDto {
        return httpClient.post("$baseUrl/employees") {
            contentType(ContentType.Application.Json)
            setBody(employee)
        }.body()
    }

    suspend fun deleteEmployee(employeeId: Int) {
        httpClient.delete("$baseUrl/employees/$employeeId")
    }

    suspend fun requestVacation(vacation: VacationDto): VacationDto {
        return httpClient.post("$baseUrl/vacations") {
            contentType(ContentType.Application.Json)
            setBody(vacation)
        }.body()
    }

    suspend fun respondToVacation(vacationId: Int, approve: Boolean): VacationDto {
        val path = if (approve) "approve" else "decline"
        return httpClient.post("$baseUrl/vacations/$vacationId/$path") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getEmployeeProfile(employeeId: Int): EmployeeDto {
        return httpClient.get("$baseUrl/employees/$employeeId").body()
    }
}
