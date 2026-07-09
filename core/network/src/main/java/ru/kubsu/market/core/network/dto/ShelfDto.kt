package ru.kubsu.market.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ShelfDto(
    val shelfId: Int? = null,
    val name: String? = null,
    val storageLocation: StorageLocationDto? = null,
    val type: String? = null
)
