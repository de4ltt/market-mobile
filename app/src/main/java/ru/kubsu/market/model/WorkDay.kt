package ru.kubsu.market.model

import kotlinx.serialization.Serializable
import ru.kubsu.market.ui.cringe.serializer.BigDecimalSerializer
import ru.kubsu.market.ui.cringe.serializer.KxLocalDateTimeSerializer
import ru.kubsu.market.ui.cringe.serializer.LocalDateSerializer
import ru.kubsu.market.ui.cringe.serializer.LocalDateTimeSerializer
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class WorkDay(
    val workDayId: Int? = null,
    val employeeId: Int? = null,
    @Serializable(LocalDateSerializer::class)
    val date: LocalDate? = null,
    @Serializable(KxLocalDateTimeSerializer::class)
    val checkIn: LocalDateTime? = null,
    @Serializable(KxLocalDateTimeSerializer::class)
    val checkOut: LocalDateTime? = null,
    @Serializable(BigDecimalSerializer::class)
    val hoursWorked: BigDecimal? = null,
    @Serializable(BigDecimalSerializer::class)
    val overtime: BigDecimal? = null,
    @Serializable(BigDecimalSerializer::class)
    val underwork: BigDecimal? = null
)
