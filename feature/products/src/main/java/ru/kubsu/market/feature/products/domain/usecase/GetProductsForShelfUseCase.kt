package ru.kubsu.market.feature.products.domain.usecase

import ru.kubsu.market.core.model.Product
import ru.kubsu.market.feature.products.domain.repository.ProductsRepository

class GetProductsForShelfUseCase(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(shelfId: Int): List<Product> {
        return repository.getProductsForShelf(shelfId)
    }
}
