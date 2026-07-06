package ru.kubsu.market.core.model

import kotlinx.serialization.Serializable
import ru.kubsu.market.core.common.serializer.LocalDateSerializer
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
) : IItemRepresentable {

    override val displayName: String
        get() = "${vacationId ?: "-"} | ${type ?: "-"}"

    override val displayFields: Map<String, String>
        get() = mapOf(
            "ID отпуска" to (vacationId?.toString() ?: "-"),
            "ID сотрудника" to (employeeId?.toString() ?: "-"),
            "Тип" to (VacationType.entries.find { it.name.equals(type, true) }
                ?: VacationType.VACATION).title,
            "Дата начала" to (startDate?.toString() ?: "-"),
            "Дата окончания" to (endDate?.toString() ?: "-"),
            "Одобрен" to if (approved) "Да" else "Нет",
            "Рассмотрен" to if (reviewed) "Да" else "Нет"
        )

    companion object {
        val className = "Отпуска"
    }
}
