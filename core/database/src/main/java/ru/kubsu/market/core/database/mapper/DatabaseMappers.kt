package ru.kubsu.market.core.database.mapper

import ru.kubsu.market.core.database.entity.EmployeeEntity
import ru.kubsu.market.core.database.entity.PositionEntity
import ru.kubsu.market.core.database.entity.VacationEntity
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Position
import ru.kubsu.market.core.model.Vacation
import java.time.LocalDate

// Position Entity Mappers
fun PositionEntity.toDomain() = Position(
    positionId = positionId,
    name = name,
    description = description,
    monthlyHours = monthlyHours,
    salaryRate = salaryRate
)

fun Position.toEntity() = PositionEntity(
    positionId = positionId ?: 0,
    name = name,
    description = description,
    monthlyHours = monthlyHours,
    salaryRate = salaryRate
)

// Employee Entity Mappers
fun EmployeeEntity.toDomain() = Employee(
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

fun Employee.toEntity() = EmployeeEntity(
    employeeId = employeeId ?: 0,
    fullName = fullName,
    passportSeries = passportSeries,
    passportNumber = passportNumber,
    registrationAddress = registrationAddress,
    birthDate = birthDate,
    position = position.toEntity(),
    department = department,
    login = login,
    password = password,
    role = role,
    workPhone = workPhone,
    personalPhone = personalPhone,
    email = email
)

// Vacation Entity Mappers
fun VacationEntity.toDomain() = Vacation(
    vacationId = vacationId,
    employeeId = employeeId,
    type = type,
    startDate = startDate,
    endDate = endDate,
    approved = approved,
    reviewed = reviewed
)

fun Vacation.toEntity() = VacationEntity(
    vacationId = vacationId ?: 0,
    employeeId = employeeId ?: 0,
    type = type ?: "",
    startDate = startDate ?: LocalDate.now(),
    endDate = endDate ?: LocalDate.now(),
    approved = approved,
    reviewed = reviewed
)
