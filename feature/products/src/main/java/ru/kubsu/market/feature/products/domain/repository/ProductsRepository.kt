package ru.kubsu.market.feature.products.domain.repository

import ru.kubsu.market.core.model.Product
import ru.kubsu.market.core.model.ProductPrice
import ru.kubsu.market.core.model.Shelf
import ru.kubsu.market.core.model.StorageLocation
import ru.kubsu.market.core.model.pricing.PriceFormationResult

interface ProductsRepository {
    suspend fun getProducts(): List<Product>
    suspend fun getProductPrices(productIds: List<Int>): List<ProductPrice>
    suspend fun formOrder(employeeId: Int): PriceFormationResult
    suspend fun getStorageLocations(): List<StorageLocation>
    suspend fun getShelves(storageLocationId: Int): List<Shelf>
    suspend fun getProductsForShelf(shelfId: Int): List<Product>
}
