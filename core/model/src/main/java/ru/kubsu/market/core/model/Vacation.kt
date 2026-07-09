package ru.kubsu.market.core.model

import java.time.LocalDate

data class Vacation(
    val vacationId: Int? = null,
    val employeeId: Int? = null,
    val type: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val approved: Boolean = false,
    val reviewed: Boolean = false
) {
    companion object {
        val className = "Отпуска"
    }
}
