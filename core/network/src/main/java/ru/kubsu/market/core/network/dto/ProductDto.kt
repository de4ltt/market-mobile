package ru.kubsu.market.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val productId: Int? = null,
    val name: String,
    val manufacturerName: String,
    val manufacturerCountry: String,
    val manufacturerCode: String,
    val size: String,
    val unit: String,
    val barcode: String? = null,
    val additionalInfo: String? = null,
    val storageRequirement: String? = "regular"
)
