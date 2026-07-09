package ru.kubsu.market.core.network.dto

import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class ReceivedProductDto(
    val receivedProductId: Int? = null,
    val employeeId: Int,
    val product: ProductDto,
    @Serializable(LocalDateSerializer::class)
    val date: LocalDate,
    val status: String,
    val quantity: Int,
    @Serializable(LocalDateSerializer::class)
    val expirationDate: LocalDate,
    val comment: String? = null
)
