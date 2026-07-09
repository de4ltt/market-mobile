package ru.kubsu.market.core.network.dto

import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class PositionDto(
    val positionId: Int? = null,
    val name: String,
    val description: String,
    val monthlyHours: Int,
    @Serializable(with = BigDecimalSerializer::class)
    val salaryRate: BigDecimal
)
