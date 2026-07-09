package ru.kubsu.market.core.network.dto

import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class ConfirmReportRequestDto(
    val directorComment: String? = null,
    @Serializable(BigDecimalSerializer::class)
    val manualOvertime: BigDecimal? = null,
    @Serializable(BigDecimalSerializer::class)
    val manualUnderwork: BigDecimal? = null
)
