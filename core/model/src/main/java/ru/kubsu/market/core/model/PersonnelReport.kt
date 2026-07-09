package ru.kubsu.market.core.model

import java.math.BigDecimal
import java.time.LocalDate

data class PersonnelReport(
    val personnelReportId: Int? = null,
    val date: LocalDate? = null,
    val directorId: Int? = null,
    val employeeId: Int? = null,
    val status: String? = null,
    val totalHours: BigDecimal? = BigDecimal.ZERO,
    val overtime: BigDecimal? = BigDecimal.ZERO,
    val underwork: BigDecimal? = BigDecimal.ZERO,
    val comment: String? = null
) {
    companion object {
        val className = "Отчеты"
    }
}
