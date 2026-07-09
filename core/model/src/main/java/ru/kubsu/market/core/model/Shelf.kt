package ru.kubsu.market.core.model

data class Shelf(
    val shelfId: Int? = null,
    val name: String? = null,
    val storageLocation: StorageLocation? = null,
    val type: String? = null
) {
    companion object {
        val className = "Полки"
    }
}
