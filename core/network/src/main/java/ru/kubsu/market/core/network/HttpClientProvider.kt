package ru.kubsu.market.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import ru.kubsu.market.core.network.dto.RefreshRequest
import ru.kubsu.market.core.network.dto.TokenResponse

class HttpClientProvider(
    private val userPrefs: UserPreferencesRepository
) {
    fun create(): HttpClient {
        return HttpClient {
            expectSuccess = true

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HTTP LOG: $message")
                    }
                }
                level = LogLevel.ALL
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val access = userPrefs.accessToken.first()
                        val refresh = userPrefs.refreshToken.first()
                        if (access != null && refresh != null) {
                            BearerTokens(access, refresh)
                        } else null
                    }
                    refreshTokens {
                        val oldRefresh = userPrefs.refreshToken.first()
                        if (oldRefresh == null) {
                            userPrefs.clearTokens()
                            null
                        } else {
                            try {
                                val response = client.post("${ApiConfig.BASE_URL_GATE}/refresh") {
                                    contentType(ContentType.Application.Json)
                                    setBody(RefreshRequest(oldRefresh))
                                }.body<TokenResponse>()
                                userPrefs.saveTokens(response.access_token, response.refresh_token)
                                BearerTokens(response.access_token, response.refresh_token)
                            } catch (e: Exception) {
                                userPrefs.clearTokens()
                                null
                            }
                        }
                    }
                }
            }
        }
    }
}
