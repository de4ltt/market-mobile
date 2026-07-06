package ru.kubsu.market.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kubsu.market.core.model.Vacation
import java.time.LocalDate

@Entity(tableName = "vacations")
data class VacationEntity(
    @PrimaryKey val vacationId: Int,
    val employeeId: Int,
    val type: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val approved: Boolean,
    val reviewed: Boolean
) {
    fun toDomain() = Vacation(
        vacationId = vacationId,
        employeeId = employeeId,
        type = type,
        startDate = startDate,
        endDate = endDate,
        approved = approved,
        reviewed = reviewed
    )

    companion object {
        fun fromDomain(vacation: Vacation) = VacationEntity(
            vacationId = vacation.vacationId ?: 0,
            employeeId = vacation.employeeId ?: 0,
            type = vacation.type ?: "",
            startDate = vacation.startDate ?: LocalDate.now(),
            endDate = vacation.endDate ?: LocalDate.now(),
            approved = vacation.approved,
            reviewed = vacation.reviewed
        )
    }
}
