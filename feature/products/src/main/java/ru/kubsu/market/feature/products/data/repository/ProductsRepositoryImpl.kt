package ru.kubsu.market.feature.products.data.repository

import ru.kubsu.market.core.model.Product
import ru.kubsu.market.core.model.ProductPrice
import ru.kubsu.market.core.model.Shelf
import ru.kubsu.market.core.model.StorageLocation
import ru.kubsu.market.core.model.pricing.PriceFormationResult
import ru.kubsu.market.core.network.api.ProductsApi
import ru.kubsu.market.core.network.mapper.toDomain
import ru.kubsu.market.feature.products.domain.repository.ProductsRepository
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productsApi: ProductsApi
) : ProductsRepository {

    override suspend fun getProducts(): List<Product> {
        return productsApi.getProducts().map { it.toDomain() }
    }

    override suspend fun getProductPrices(productIds: List<Int>): List<ProductPrice> {
        return productsApi.getProductPrices(productIds).map { it.toDomain() }
    }

    override suspend fun formOrder(employeeId: Int): PriceFormationResult {
        return productsApi.formOrder(employeeId).toDomain()
    }

    override suspend fun getStorageLocations(): List<StorageLocation> {
        return productsApi.getStorageLocations().map { it.toDomain() }
    }

    override suspend fun getShelves(storageLocationId: Int): List<Shelf> {
        return productsApi.getShelves(storageLocationId).map { it.toDomain() }
    }

    override suspend fun getProductsForShelf(shelfId: Int): List<Product> {
        return productsApi.getProductsForShelf(shelfId).map { it.toDomain() }
    }
}
