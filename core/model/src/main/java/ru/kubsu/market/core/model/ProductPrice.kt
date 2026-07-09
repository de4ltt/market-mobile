package ru.kubsu.market.core.model

import java.math.BigDecimal
import java.time.LocalDate

data class ProductPrice(
    val productId: Int,
    val productName: String,
    val currentPrice: BigDecimal,
    val regularPrice: BigDecimal,
    val labelType: String,
    val effectiveDate: LocalDate
)
