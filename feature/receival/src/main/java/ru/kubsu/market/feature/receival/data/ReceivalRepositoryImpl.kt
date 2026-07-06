package ru.kubsu.market.feature.receival.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kubsu.market.core.model.ReceivedProduct
import ru.kubsu.market.core.network.ApiConfig
import ru.kubsu.market.feature.receival.domain.ReceivalRepository

class ReceivalRepositoryImpl(
    private val httpClient: HttpClient
) : ReceivalRepository {

    private val baseUrl = ApiConfig.BASE_URL

    override suspend fun getReceivedProductsToResolve(): List<ReceivedProduct> {
        return httpClient.get("$baseUrl/received-products/to-resolve").body<List<ReceivedProduct>>()
    }

    override suspend fun resolveProducts(
        acceptedProducts: List<ReceivedProduct>,
        refusedProducts: List<ReceivedProduct>,
        employeeId: Int
    ) {
        // These requests were commented out in the original AppViewModel implementation.
        // Keeping them commented out as in the original code, but ready for future enablement.
        /*
        httpClient.post("$baseUrl/received-products/reject") {
            contentType(ContentType.Application.Json)
            setBody(refusedProducts)
        }
        httpClient.post("$baseUrl/received-products/$employeeId/accept") {
            contentType(ContentType.Application.Json)
            setBody(acceptedProducts)
        }
        */
    }
}
