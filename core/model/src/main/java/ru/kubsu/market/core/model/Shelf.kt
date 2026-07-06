package ru.kubsu.market.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Shelf(
    val shelfId: Int? = null,
    val name: String? = null,
    val storageLocation: StorageLocation? = null,
    val type: String? = null
) : IItemRepresentable {

    override val displayName: String
        get() = "${shelfId ?: "-"} | ${name ?: "Название"}"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "Название" to (name ?: "Название"),
            "Тип" to (type ?: "Обычный")
        )

    companion object {
        val className = "Полки"
    }
}
