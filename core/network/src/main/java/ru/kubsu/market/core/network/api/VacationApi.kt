package ru.kubsu.market.core.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kubsu.market.core.network.ApiConfig
import ru.kubsu.market.core.network.dto.VacationDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VacationApi @Inject constructor(
    private val httpClient: HttpClient
) {
    private val baseUrl = ApiConfig.BASE_URL

    suspend fun getVacations(userId: Int): List<VacationDto> {
        return httpClient.get("$baseUrl/vacations/employee/$userId").body()
    }

    suspend fun getAvailableVacationDays(userId: Int, year: Int): Long {
        return httpClient.get("$baseUrl/vacations/employee/$userId/$year/available").body()
    }

    suspend fun requestVacation(vacation: VacationDto): VacationDto {
        return httpClient.post("$baseUrl/vacations") {
            contentType(ContentType.Application.Json)
            setBody(vacation)
        }.body()
    }
}
