package ru.kubsu.market.core.model

import java.math.BigDecimal

data class Position(
    val positionId: Int? = null,
    val name: String,
    val description: String,
    val monthlyHours: Int,
    val salaryRate: BigDecimal
) {
    companion object {
        val className = "Должности"
    }
}
