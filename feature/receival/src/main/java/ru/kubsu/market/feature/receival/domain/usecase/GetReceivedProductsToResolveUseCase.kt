package ru.kubsu.market.feature.receival.domain.usecase

import ru.kubsu.market.core.model.ReceivedProduct
import ru.kubsu.market.feature.receival.domain.ReceivalRepository

import javax.inject.Inject

class GetReceivedProductsToResolveUseCase @Inject constructor(
    private val repository: ReceivalRepository
) {
    suspend operator fun invoke(): List<ReceivedProduct> {
        return repository.getReceivedProductsToResolve()
    }
}
