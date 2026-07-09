package ru.kubsu.market.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "vacations")
data class VacationEntity(
    @PrimaryKey val vacationId: Int,
    val employeeId: Int,
    val type: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val approved: Boolean,
    val reviewed: Boolean
)
