package ru.kubsu.market.core.model

import kotlinx.serialization.KSerializer
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
) : IDictionaryItem {

    override val endpoint: String
        get() = "trucks"
    override val className: String
        get() = "Грузовики"
    override val serializer: KSerializer<out IDictionaryItem>
        get() = serializer()

    companion object {
        val className = "Грузовики"
    }
}
