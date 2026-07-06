package ru.kubsu.market.core.model

import kotlinx.serialization.Serializable

@Serializable
data class StorageLocation(
    val storageLocationId: Int? = null,
    val name: String,
    val type: String,
    val address: String
) : IItemRepresentable {

    override val displayName: String
        get() = "${storageLocationId ?: "-"} | $name"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "ID" to "${storageLocationId ?: "-"}",
            "Название" to name,
            "Тип" to type,
            "Адрес" to address,
        )

    companion object {
        val className = "Места хранения"
    }
}
