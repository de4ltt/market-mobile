package ru.kubsu.market.core.model

import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class Truck(
    val truckId: Int? = null,
    val licencePlate: String,
    val model: String,
    @Serializable(with = BigDecimalSerializer::class)
    val capacity: BigDecimal,
    val driverId: Int
) : IItemRepresentable, IDictionaryItem {

    override val displayName: String
        get() = "${truckId ?: "-"} | $licencePlate"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "Госномер" to licencePlate,
            "Модель" to model,
            "Грузоподъёмность" to capacity.toString(),
            "Водитель Id" to driverId.toString(),
        )

    override val endpoint: String
        get() = "trucks"
    override val className: String
        get() = "Грузовики"

    override fun getItems(fetcher: IDictionaryFetcher) {
        fetcher.getDictionaryItems(this)
    }

    companion object {
        val className = "Грузовики"
    }
}
