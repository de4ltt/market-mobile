package ru.kubsu.market.core.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kubsu.market.core.network.ApiConfig
import ru.kubsu.market.core.network.dto.ProductDto
import ru.kubsu.market.core.network.dto.ProductPriceDto
import ru.kubsu.market.core.network.dto.ShelfDto
import ru.kubsu.market.core.network.dto.StorageLocationDto
import ru.kubsu.market.core.network.dto.PriceFormationResultDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsApi @Inject constructor(
    private val httpClient: HttpClient
) {
    private val baseUrl = ApiConfig.BASE_URL

    suspend fun getProducts(): List<ProductDto> {
        return httpClient.get("$baseUrl/products").body()
    }

    suspend fun getProductPrices(productIds: List<Int>): List<ProductPriceDto> {
        return httpClient.post("$baseUrl/pricing/products") {
            contentType(ContentType.Application.Json)
            setBody(productIds)
        }.body()
    }

    suspend fun formOrder(employeeId: Int): PriceFormationResultDto {
        return httpClient.get("$baseUrl/pricing/$employeeId/make-order").body()
    }

    suspend fun getStorageLocations(): List<StorageLocationDto> {
        return httpClient.get("$baseUrl/storage-locations").body()
    }

    suspend fun getShelves(storageLocationId: Int): List<ShelfDto> {
        return httpClient.get("$baseUrl/storage-locations/$storageLocationId/shelves").body()
    }

    suspend fun getProductsForShelf(shelfId: Int): List<ProductDto> {
        return httpClient.get("$baseUrl/shelves/$shelfId/products").body()
    }
}
