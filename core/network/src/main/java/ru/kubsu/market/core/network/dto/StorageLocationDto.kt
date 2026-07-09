package ru.kubsu.market.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class StorageLocationDto(
    val storageLocationId: Int? = null,
    val name: String,
    val type: String,
    val address: String
)
