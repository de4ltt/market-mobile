package ru.kubsu.market.core.model

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.LocalDateSerializer
import ru.kubsu.market.core.ui.theme.Colors
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
) : IItemRepresentable() {

    @Composable
    override fun ShortContent() {
        Text(
            modifier = Modifier.basicMarquee(),
            text = "$employeeId | $fullName",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Colors.WHITE
        )
    }

    @Composable
    override fun FullContent() {
        val map = mapOf(
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
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FieldsRepresentation(map = map)
        }
    }

    companion object {
        val className = "Сотрудники"
    }
}
