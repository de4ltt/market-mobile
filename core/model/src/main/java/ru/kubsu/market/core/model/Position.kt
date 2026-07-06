package ru.kubsu.market.core.model

import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class Position(
    val positionId: Int? = null,
    val name: String,
    val description: String,
    val monthlyHours: Int,
    @Serializable(with = BigDecimalSerializer::class)
    val salaryRate: BigDecimal
) : IItemRepresentable {

    override val displayName: String
        get() = "${positionId ?: "-"} | $name"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "Должность" to name,
            "Описание" to description,
            "Месячные часы" to monthlyHours.toString(),
            "Ставка" to salaryRate.toString(),
        )

    companion object {
        val className = "Должности"
    }
}
