package ru.kubsu.market.feature.products.domain.usecase

import ru.kubsu.market.core.model.Product
import ru.kubsu.market.feature.products.domain.repository.ProductsRepository

import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(): List<Product> {
        return repository.getProducts()
    }
}
