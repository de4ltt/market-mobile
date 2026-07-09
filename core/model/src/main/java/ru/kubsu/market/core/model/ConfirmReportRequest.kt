package ru.kubsu.market.core.model

import java.math.BigDecimal

data class ConfirmReportRequest(
    val directorComment: String? = null,
    val manualOvertime: BigDecimal? = null,
    val manualUnderwork: BigDecimal? = null
)
