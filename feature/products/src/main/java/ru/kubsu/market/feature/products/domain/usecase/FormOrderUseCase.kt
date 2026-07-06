package ru.kubsu.market.feature.products.domain.usecase

import ru.kubsu.market.core.model.pricing.PriceFormationResult
import ru.kubsu.market.feature.products.domain.repository.ProductsRepository

import javax.inject.Inject

class FormOrderUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(employeeId: Int): PriceFormationResult {
        return repository.formOrder(employeeId)
    }
}
