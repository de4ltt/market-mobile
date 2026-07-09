package ru.kubsu.market.core.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import kotlinx.serialization.builtins.ListSerializer
import ru.kubsu.market.core.model.IDictionaryItem
import ru.kubsu.market.core.network.ApiConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DictionariesApi @Inject constructor(
    private val httpClient: HttpClient
) {
    private val baseUrl = ApiConfig.BASE_URL
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getItems(item: IDictionaryItem): List<IDictionaryItem> {
        val responseBody = httpClient.get("$baseUrl/${item.endpoint}").body<String>()
        return json.decodeFromString(ListSerializer(item.serializer), responseBody)
    }
}
