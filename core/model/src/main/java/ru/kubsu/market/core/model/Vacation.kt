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
data class Vacation(
    val vacationId: Int? = null,
    val employeeId: Int? = null,
    val type: String? = null,
    @Serializable(LocalDateSerializer::class)
    val startDate: LocalDate? = null,
    @Serializable(LocalDateSerializer::class)
    val endDate: LocalDate? = null,
    val approved: Boolean = false,
    val reviewed: Boolean = false
) : IItemRepresentable() {

    @Composable
    override fun ShortContent() {
        Text(
            modifier = Modifier.basicMarquee(),
            text = "$vacationId | $type",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Colors.WHITE
        )
    }

    @Composable
    override fun FullContent() {
        val map = mapOf(
            "ID отпуска" to (vacationId?.toString() ?: "-"),
            "ID сотрудника" to (employeeId?.toString() ?: "-"),
            "Тип" to (VacationType.entries.find { it.name.equals(type, true) }
                ?: VacationType.VACATION).title,
            "Дата начала" to (startDate?.toString() ?: "-"),
            "Дата окончания" to (endDate?.toString() ?: "-"),
            "Одобрен" to if (approved) "Да" else "Нет",
            "Рассмотрен" to if (reviewed) "Да" else "Нет"
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FieldsRepresentation(map = map)
        }
    }

    companion object {
        val className = "Отпуска"
    }
}
