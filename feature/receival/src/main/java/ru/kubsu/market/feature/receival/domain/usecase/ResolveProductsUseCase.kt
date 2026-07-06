package ru.kubsu.market.feature.receival.domain.usecase

import ru.kubsu.market.core.model.ReceivedProduct
import ru.kubsu.market.feature.receival.domain.ReceivalRepository

class ResolveProductsUseCase(
    private val repository: ReceivalRepository
) {
    suspend operator fun invoke(
        acceptedProducts: List<ReceivedProduct>,
        refusedProducts: List<ReceivedProduct>,
        employeeId: Int
    ) {
        repository.resolveProducts(acceptedProducts, refusedProducts, employeeId)
    }
}
