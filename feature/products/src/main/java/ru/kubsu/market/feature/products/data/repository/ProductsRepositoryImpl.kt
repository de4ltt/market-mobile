package ru.kubsu.market.feature.products.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kubsu.market.core.model.Product
import ru.kubsu.market.core.model.ProductPrice
import ru.kubsu.market.core.model.Shelf
import ru.kubsu.market.core.model.StorageLocation
import ru.kubsu.market.core.model.pricing.PriceFormationResult
import ru.kubsu.market.core.network.ApiConfig
import ru.kubsu.market.feature.products.domain.repository.ProductsRepository

class ProductsRepositoryImpl(
    private val httpClient: HttpClient
) : ProductsRepository {

    private val baseUrl = ApiConfig.BASE_URL

    override suspend fun getProducts(): List<Product> {
        return httpClient.get("$baseUrl/products").body<List<Product>>()
    }

    override suspend fun getProductPrices(productIds: List<Int>): List<ProductPrice> {
        return httpClient.post("$baseUrl/pricing/products") {
            contentType(ContentType.Application.Json)
            setBody(productIds)
        }.body<List<ProductPrice>>()
    }

    override suspend fun formOrder(employeeId: Int): PriceFormationResult {
        return httpClient.get("$baseUrl/pricing/$employeeId/make-order").body<PriceFormationResult>()
    }

    override suspend fun getStorageLocations(): List<StorageLocation> {
        return httpClient.get("$baseUrl/storage-locations").body<List<StorageLocation>>()
    }

    override suspend fun getShelves(storageLocationId: Int): List<Shelf> {
        return httpClient.get("$baseUrl/storage-locations/$storageLocationId/shelves").body<List<Shelf>>()
    }

    override suspend fun getProductsForShelf(shelfId: Int): List<Product> {
        return httpClient.get("$baseUrl/shelves/$shelfId/products").body<List<Product>>()
    }
}
