package ru.kubsu.market.core.model

import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class ConfirmReportRequest(
    val directorComment: String? = null,
    @Serializable(BigDecimalSerializer::class)
    val manualOvertime: BigDecimal? = null,
    @Serializable(BigDecimalSerializer::class)
    val manualUnderwork: BigDecimal? = null
)
