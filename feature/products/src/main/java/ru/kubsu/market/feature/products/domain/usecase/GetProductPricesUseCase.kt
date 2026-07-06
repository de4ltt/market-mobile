package ru.kubsu.market.feature.products.domain.usecase

import ru.kubsu.market.core.model.ProductPrice
import ru.kubsu.market.feature.products.domain.repository.ProductsRepository

class GetProductPricesUseCase(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(productIds: List<Int>): List<ProductPrice> {
        return repository.getProductPrices(productIds)
    }
}
