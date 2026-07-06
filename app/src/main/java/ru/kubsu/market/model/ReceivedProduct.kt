package ru.kubsu.market.model

import kotlinx.serialization.Serializable
import ru.kubsu.market.ui.cringe.serializer.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class ReceivedProduct(
    val receivedProductId: Int? = null,
    val employeeId: Int,
    val product: Product,
    @Serializable(LocalDateSerializer::class)
    val date: LocalDate,
    val status: String,
    val quantity: Int,
    @Serializable(LocalDateSerializer::class)
    val expirationDate: LocalDate,
    val comment: String? = null
)
