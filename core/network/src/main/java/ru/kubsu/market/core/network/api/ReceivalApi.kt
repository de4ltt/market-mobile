package ru.kubsu.market.core.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.kubsu.market.core.network.ApiConfig
import ru.kubsu.market.core.network.dto.ReceivedProductDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceivalApi @Inject constructor(
    private val httpClient: HttpClient
) {
    private val baseUrl = ApiConfig.BASE_URL

    suspend fun getReceivedProductsToResolve(): List<ReceivedProductDto> {
        return httpClient.get("$baseUrl/received-products/to-resolve").body()
    }
}
