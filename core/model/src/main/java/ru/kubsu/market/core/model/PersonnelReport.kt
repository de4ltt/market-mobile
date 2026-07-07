package ru.kubsu.market.core.model

import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.BigDecimalSerializer
import ru.kubsu.market.core.common.serializer.LocalDateSerializer
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
) : IItemRepresentable {

    override val displayName: String
        get() = "${personnelReportId ?: "-"} | ${date?.toString() ?: "—"}"

    override val displayFields: Map<String, String>
        get() = linkedMapOf(
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

    companion object {
        val className = "Отчёты по персоналу"
    }
}
