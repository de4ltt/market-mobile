package ru.kubsu.market.core.model

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class WorkDay(
    val workDayId: Int? = null,
    val employeeId: Int? = null,
    val date: LocalDate? = null,
    val checkIn: LocalDateTime? = null,
    val checkOut: LocalDateTime? = null,
    val hoursWorked: BigDecimal? = null,
    val overtime: BigDecimal? = null,
    val underwork: BigDecimal? = null
)
