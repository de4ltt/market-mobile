package ru.kubsu.market.feature.products.domain.usecase

import ru.kubsu.market.core.model.Shelf
import ru.kubsu.market.feature.products.domain.repository.ProductsRepository

import javax.inject.Inject

class GetShelvesUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(storageLocationId: Int): List<Shelf> {
        return repository.getShelves(storageLocationId)
    }
}
