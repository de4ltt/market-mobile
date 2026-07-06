package ru.kubsu.market.feature.dictionaries.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.kubsu.market.core.model.ContactPerson
import ru.kubsu.market.core.model.Counterparty
import ru.kubsu.market.core.model.IDictionaryItem
import ru.kubsu.market.core.model.IItemRepresentable
import ru.kubsu.market.core.model.SupplyContract
import ru.kubsu.market.core.model.SupplyContractItem
import ru.kubsu.market.core.model.Truck
import ru.kubsu.market.core.network.ApiConfig
import ru.kubsu.market.feature.dictionaries.domain.DictionariesRepository

class DictionariesRepositoryImpl(
    private val httpClient: HttpClient
) : DictionariesRepository {

    private val baseUrl = ApiConfig.BASE_URL

    override suspend fun getItems(item: IDictionaryItem): List<IItemRepresentable> {
        return when (item) {
            is ContactPerson -> httpClient.get("$baseUrl/${item.endpoint}").body<List<ContactPerson>>()
            is Counterparty -> httpClient.get("$baseUrl/${item.endpoint}").body<List<Counterparty>>()
            is SupplyContract -> httpClient.get("$baseUrl/${item.endpoint}").body<List<SupplyContract>>()
            is SupplyContractItem -> httpClient.get("$baseUrl/${item.endpoint}").body<List<SupplyContractItem>>()
            is Truck -> httpClient.get("$baseUrl/${item.endpoint}").body<List<Truck>>()
            else -> throw IllegalArgumentException("Unknown dictionary item type")
        }
    }
}
