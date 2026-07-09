package ru.kubsu.market.feature.products.domain.usecase

import ru.kubsu.market.core.model.ProductPrice
import ru.kubsu.market.feature.products.domain.repository.ProductsRepository
import java.time.LocalDate
import javax.inject.Inject

class FormOrderUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(employeeId: Int): Map<Int, ProductPrice> {
        val result = repository.formOrder(employeeId)
        return result.productPrices.map { orderPrice ->
            ProductPrice(
                productId = orderPrice.productId,
                productName = "",
                currentPrice = orderPrice.appliedPrice,
                regularPrice = orderPrice.regularPrice,
                labelType = orderPrice.priceTagType.name,
                effectiveDate = LocalDate.now()
            )
        }.associateBy { it.productId }
    }
}
