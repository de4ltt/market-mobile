package ru.kubsu.market.core.model

import kotlin.random.Random

data class Product(
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
) {
    val expirationDays: Long = Random.nextLong(60)

    companion object {
        val className = "Продукты"
    }
}
