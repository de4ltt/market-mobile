package ru.kubsu.market.model

import kotlinx.serialization.Serializable
import ru.kubsu.market.ui.cringe.serializer.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class ConfirmReportRequest(
    val directorComment: String? = null,
    @Serializable(BigDecimalSerializer::class)
    val manualOvertime: BigDecimal? = null, // может быть null — значит не меняем
    @Serializable(BigDecimalSerializer::class)
    val manualUnderwork: BigDecimal? = null // может быть null — значит не меняем
)