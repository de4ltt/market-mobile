package ru.kubsu.market.core.model

data class StorageLocation(
    val storageLocationId: Int? = null,
    val name: String,
    val type: String,
    val address: String
) {
    companion object {
        val className = "Места хранения"
    }
}
