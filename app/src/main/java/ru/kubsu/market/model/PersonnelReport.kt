package ru.kubsu.market.model

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
import ru.kubsu.market.ui.component.PersonnelReportStatus
import ru.kubsu.market.ui.cringe.serializer.BigDecimalSerializer
import ru.kubsu.market.ui.cringe.serializer.LocalDateSerializer
import ru.kubsu.market.ui.theme.Colors
import java.math.BigDecimal
import java.time.LocalDate

@Serializable
data class PersonnelReport(
    val personnelReportId: Int? = null,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate? = null,
    val directorId: Int? = null,
    val employeeId: Int? = null,
    val status: String? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val totalHours: BigDecimal? = BigDecimal.ZERO,
    @Serializable(with = BigDecimalSerializer::class)
    val overtime: BigDecimal? = BigDecimal.ZERO,
    @Serializable(with = BigDecimalSerializer::class)
    val underwork: BigDecimal? = BigDecimal.ZERO,
    val comment: String? = null
) : IItemRepresentable() {

    @Composable
    override fun ShortContent() {
        Text(
            modifier = Modifier.basicMarquee(),
            text = "${personnelReportId ?: "-"} | ${date?.toString() ?: "—"}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Colors.WHITE
        )
    }

    @Composable
    override fun FullContent() {
        val map = linkedMapOf(
            "Дата" to (date?.toString() ?: "—"),
            "ID директора" to (directorId?.toString() ?: "—"),
            "ID сотрудника" to (employeeId?.toString() ?: "—"),
            "Статус" to (PersonnelReportStatus.entries.find { it.name.equals(status, true) }?.title
                ?: PersonnelReportStatus.DRAFT.title),
            "Всего часов" to (totalHours?.toString() ?: "0"),
            "Переработка" to (overtime?.toString() ?: "0"),
            "Недоработка" to (underwork?.toString() ?: "0"),
            "Комментарий" to (comment ?: "—")
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FieldsRepresentation(map = map)
        }
    }

    companion object {
        val className = "Отчёты по персоналу"
    }
}
