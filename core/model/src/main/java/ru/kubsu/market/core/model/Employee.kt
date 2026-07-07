package ru.kubsu.market.core.model

import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class Employee(
    val employeeId: Int? = null,
    val fullName: String,
    val passportSeries: String,
    val passportNumber: String,
    val registrationAddress: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    val position: Position,
    val department: String,
    val login: String,
    val password: String,
    val role: String,
    val workPhone: String,
    val personalPhone: String,
    val email: String
) : IItemRepresentable {

    override val displayName: String
        get() = "${employeeId ?: "-"} | $fullName"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "ФИО" to fullName,
            "Паспорт" to "$passportSeries $passportNumber",
            "Адрес регистрации" to registrationAddress,
            "Дата рождения" to birthDate.toString(),
            "Подразделение" to department,
            "Роль" to (Role.findByRole(role) ?: Role.FIRED).title,
            "Рабочий телефон" to workPhone,
            "Личный телефон" to personalPhone,
            "Email" to email,
        )

    companion object {
        val className = "Сотрудники"
    }
}
