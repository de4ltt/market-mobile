package ru.kubsu.market.core.network.dto

import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.BigDecimalSerializer
import ru.kubsu.market.core.common.serializer.LocalDateSerializer
import java.math.BigDecimal
import java.time.LocalDate

@Serializable
data class PersonnelReportDto(
    val personnelReportId: Int? = null,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate? = null,
    val directorId: Int? = null,
    val employeeId: Int? = null,
    val status: String? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val totalHours: BigDecimal? = BigDecimal.ZERO,
    @Serializable(with = BigDecimalSerializer::class)
    val overtime: BigDecimal? = BigDecimal.ZERO,
    @Serializable(with = BigDecimalSerializer::class)
    val underwork: BigDecimal? = BigDecimal.ZERO,
    val comment: String? = null
)
