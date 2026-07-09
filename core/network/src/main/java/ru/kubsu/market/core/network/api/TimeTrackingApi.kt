package ru.kubsu.market.core.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.parameter
import ru.kubsu.market.core.network.ApiConfig
import ru.kubsu.market.core.network.dto.WorkDayDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeTrackingApi @Inject constructor(
    private val httpClient: HttpClient
) {
    private val baseUrl = ApiConfig.BASE_URL

    suspend fun isCheckedIn(userId: Int): Boolean {
        return httpClient.get("$baseUrl/time-tracking/$userId/is-checked-in").body()
    }

    suspend fun checkIn(userId: Int): WorkDayDto {
        return httpClient.post("$baseUrl/time-tracking/$userId/check-in").body()
    }

    suspend fun checkOut(userId: Int): WorkDayDto {
        return httpClient.post("$baseUrl/time-tracking/$userId/check-out").body()
    }

    suspend fun zeroOvertime(userId: Int): WorkDayDto {
        return httpClient.post("$baseUrl/time-tracking/$userId/zero-overtime").body()
    }

    suspend fun getWorkDays(employeeId: Int, startDate: String, endDate: String): List<WorkDayDto> {
        return httpClient.get("$baseUrl/time-tracking/$employeeId/work-days") {
            parameter("startDate", startDate)
            parameter("endDate", endDate)
        }.body()
    }
}
