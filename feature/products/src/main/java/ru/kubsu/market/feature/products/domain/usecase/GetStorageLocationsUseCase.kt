package ru.kubsu.market.feature.products.domain.usecase

import ru.kubsu.market.core.model.StorageLocation
import ru.kubsu.market.feature.products.domain.repository.ProductsRepository

class GetStorageLocationsUseCase(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(): List<StorageLocation> {
        return repository.getStorageLocations()
    }
}
