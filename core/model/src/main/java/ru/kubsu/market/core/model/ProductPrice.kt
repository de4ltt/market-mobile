package ru.kubsu.market.core.model

import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.BigDecimalSerializer
import ru.kubsu.market.core.common.serializer.LocalDateSerializer
import java.math.BigDecimal
import java.time.LocalDate

@Serializable
data class ProductPrice(
    val productId: Int,
    val productName: String,
    @Serializable(BigDecimalSerializer::class)
    val currentPrice: BigDecimal,
    @Serializable(BigDecimalSerializer::class)
    val regularPrice: BigDecimal,
    val labelType: String,
    @Serializable(LocalDateSerializer::class)
    val effectiveDate: LocalDate
)
