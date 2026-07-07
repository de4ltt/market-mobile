package ru.kubsu.market.feature.products.domain.usecase

import ru.kubsu.market.core.model.ProductPrice
import ru.kubsu.market.feature.products.domain.repository.ProductsRepository

import javax.inject.Inject

class GetProductPricesUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(productIds: List<Int>): List<ProductPrice> {
        return repository.getProductPrices(productIds)
    }
}
