package ru.kubsu.market.core.network.dto

import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class EmployeeDto(
    val employeeId: Int? = null,
    val fullName: String,
    val passportSeries: String,
    val passportNumber: String,
    val registrationAddress: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    val position: PositionDto,
    val department: String,
    val login: String,
    val password: String,
    val role: String,
    val workPhone: String,
    val personalPhone: String,
    val email: String
)
