package ru.kubsu.market.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "employees")
data class EmployeeEntity(
    @PrimaryKey val employeeId: Int,
    val fullName: String,
    val passportSeries: String,
    val passportNumber: String,
    val registrationAddress: String,
    val birthDate: LocalDate,
    @Embedded(prefix = "position_") val position: PositionEntity,
    val department: String,
    val login: String,
    val password: String,
    val role: String,
    val workPhone: String,
    val personalPhone: String,
    val email: String
)

data class PositionEntity(
    val positionId: Int,
    val name: String,
    val description: String,
    val monthlyHours: Int,
    val salaryRate: java.math.BigDecimal
)
