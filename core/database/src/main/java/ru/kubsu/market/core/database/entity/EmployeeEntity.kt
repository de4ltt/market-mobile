package ru.kubsu.market.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Position
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
) {
    fun toDomain() = Employee(
        employeeId = employeeId,
        fullName = fullName,
        passportSeries = passportSeries,
        passportNumber = passportNumber,
        registrationAddress = registrationAddress,
        birthDate = birthDate,
        position = position.toDomain(),
        department = department,
        login = login,
        password = password,
        role = role,
        workPhone = workPhone,
        personalPhone = personalPhone,
        email = email
    )

    companion object {
        fun fromDomain(employee: Employee) = EmployeeEntity(
            employeeId = employee.employeeId ?: 0,
            fullName = employee.fullName,
            passportSeries = employee.passportSeries,
            passportNumber = employee.passportNumber,
            registrationAddress = employee.registrationAddress,
            birthDate = employee.birthDate,
            position = PositionEntity.fromDomain(employee.position),
            department = employee.department,
            login = employee.login,
            password = employee.password,
            role = employee.role,
            workPhone = employee.workPhone,
            personalPhone = employee.personalPhone,
            email = employee.email
        )
    }
}

data class PositionEntity(
    val positionId: Int,
    val name: String,
    val description: String,
    val monthlyHours: Int,
    val salaryRate: java.math.BigDecimal
) {
    fun toDomain() = Position(
        positionId = positionId,
        name = name,
        description = description,
        monthlyHours = monthlyHours,
        salaryRate = salaryRate
    )

    companion object {
        fun fromDomain(position: Position) = PositionEntity(
            positionId = position.positionId ?: 0,
            name = position.name,
            description = position.description,
            monthlyHours = position.monthlyHours,
            salaryRate = position.salaryRate
        )
    }
}
