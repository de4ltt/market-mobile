package ru.kubsu.market.feature.receival.domain

import ru.kubsu.market.core.model.ReceivedProduct

interface ReceivalRepository {
    suspend fun getReceivedProductsToResolve(): List<ReceivedProduct>
    suspend fun resolveProducts(
        acceptedProducts: List<ReceivedProduct>,
        refusedProducts: List<ReceivedProduct>,
        employeeId: Int
    )
}
