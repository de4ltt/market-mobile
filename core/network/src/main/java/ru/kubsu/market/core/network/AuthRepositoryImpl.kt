package ru.kubsu.market.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parameters
import ru.kubsu.market.core.network.dto.RefreshRequest
import ru.kubsu.market.core.network.dto.TokenResponse
import ru.kubsu.market.core.network.dto.UserResponse

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val userPrefs: UserPreferencesRepository
) : AuthRepository {

    override suspend fun login(login: String, password: String): TokenResponse {
        val response = httpClient.submitForm(
            url = "${ApiConfig.BASE_URL_GATE}/login",
            formParameters = parameters {
                append("employee_login", login)
                append("password", password)
            }
        ).body<TokenResponse>()
        userPrefs.saveTokens(response.access_token, response.refresh_token)
        return response
    }

    override suspend fun getMe(): UserResponse {
        return httpClient.get("${ApiConfig.BASE_URL_GATE}/me").body<UserResponse>()
    }

    override suspend fun logout() {
        userPrefs.clearTokens()
    }

    override suspend fun refresh(refreshToken: String): TokenResponse {
        val response = httpClient.post("${ApiConfig.BASE_URL_GATE}/refresh") {
            contentType(ContentType.Application.Json)
            setBody(RefreshRequest(refreshToken))
        }.body<TokenResponse>()
        userPrefs.saveTokens(response.access_token, response.refresh_token)
        return response
    }
}
