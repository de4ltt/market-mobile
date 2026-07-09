package ru.kubsu.market.core.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parameters
import ru.kubsu.market.core.network.ApiConfig
import ru.kubsu.market.core.network.dto.EmployeeDto
import ru.kubsu.market.core.network.dto.RefreshRequest
import ru.kubsu.market.core.network.dto.TokenResponse
import ru.kubsu.market.core.network.dto.UserResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthApi @Inject constructor(
    private val httpClient: HttpClient
) {
    suspend fun login(login: String, password: String): TokenResponse {
        return httpClient.submitForm(
            url = "${ApiConfig.BASE_URL_GATE}/login",
            formParameters = parameters {
                append("employee_login", login)
                append("password", password)
            }
        ).body()
    }

    suspend fun getMe(): UserResponse {
        return httpClient.get("${ApiConfig.BASE_URL_GATE}/me").body()
    }

    suspend fun refresh(refreshToken: String): TokenResponse {
        return httpClient.post("${ApiConfig.BASE_URL_GATE}/refresh") {
            contentType(ContentType.Application.Json)
            setBody(RefreshRequest(refreshToken))
        }.body()
    }

    suspend fun getEmployeeProfile(employeeId: Int): EmployeeDto {
        return httpClient.get("${ApiConfig.BASE_URL}/employees/$employeeId").body()
    }
}
