package ru.kubsu.market.feature.products.domain.usecase

import ru.kubsu.market.core.model.Shelf
import ru.kubsu.market.feature.products.domain.repository.ProductsRepository

class GetShelvesUseCase(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(storageLocationId: Int): List<Shelf> {
        return repository.getShelves(storageLocationId)
    }
}
