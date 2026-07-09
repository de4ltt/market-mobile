package ru.kubsu.market.core.model

import java.time.LocalDate

data class Employee(
    val employeeId: Int? = null,
    val fullName: String,
    val passportSeries: String,
    val passportNumber: String,
    val registrationAddress: String,
    val birthDate: LocalDate,
    val position: Position,
    val department: String,
    val login: String,
    val password: String,
    val role: String,
    val workPhone: String,
    val personalPhone: String,
    val email: String
) {
    companion object {
        val className = "Сотрудники"
    }
}
