package ru.kubsu.market.core.model

import kotlinx.serialization.Serializable
import java.time.LocalDate
import kotlin.random.Random

@Serializable
data class Product(
    val productId: Int? = null,
    val name: String,
    val manufacturerName: String,
    val manufacturerCountry: String,
    val manufacturerCode: String,
    val size: String,
    val unit: String,
    override val barcode: String? = null,
    val additionalInfo: String? = null,
    val storageRequirement: String? = "regular"
): IItemRepresentable {

    override val displayName: String
        get() = "${productId ?: "-"} | $name"

    val expirationDays: Long = Random.nextLong(60)

    override val displayFields: Map<String, String>
        get() = mapOf(
            "Производитель" to manufacturerName,
            "Код производителя" to manufacturerCode,
            "Страна" to manufacturerCountry,
            "Годен до" to LocalDate.now().plusDays(expirationDays).toString(),
            "Размеры" to size,
            "Единица измерения" to unit,
            "Технический срок годности" to expirationDays.toString()
        )

    companion object {
        val className = "Продукты"
    }
}
