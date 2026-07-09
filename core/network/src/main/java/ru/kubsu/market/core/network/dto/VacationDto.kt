package ru.kubsu.market.core.network.dto

import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class VacationDto(
    val vacationId: Int? = null,
    val employeeId: Int? = null,
    val type: String? = null,
    @Serializable(LocalDateSerializer::class)
    val startDate: LocalDate? = null,
    @Serializable(LocalDateSerializer::class)
    val endDate: LocalDate? = null,
    val approved: Boolean = false,
    val reviewed: Boolean = false
)
