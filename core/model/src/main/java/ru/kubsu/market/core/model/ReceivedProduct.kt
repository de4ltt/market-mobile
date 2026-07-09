package ru.kubsu.market.core.model

import java.time.LocalDate

data class ReceivedProduct(
    val receivedProductId: Int? = null,
    val employeeId: Int,
    val product: Product,
    val date: LocalDate,
    val status: String,
    val quantity: Int,
    val expirationDate: LocalDate,
    val comment: String? = null
)
