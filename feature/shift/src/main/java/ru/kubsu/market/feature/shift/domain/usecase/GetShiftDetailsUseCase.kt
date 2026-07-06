package ru.kubsu.market.feature.shift.domain.usecase

import java.time.LocalDate
import ru.kubsu.market.feature.shift.domain.ShiftRepository
import ru.kubsu.market.feature.shift.domain.model.ShiftDetails

class GetShiftDetailsUseCase(
    private val repository: ShiftRepository
) {
    suspend operator fun invoke(employeeId: Int): ShiftDetails {
        val employee = repository.getEmployeeProfile(employeeId)
        val startDate = "${LocalDate.now().minusDays(LocalDate.now().dayOfWeek.value.toLong())}"
        val endDate = "${LocalDate.now()}"
        val workDays = repository.getWorkDays(employeeId, startDate, endDate)
        val hours = workDays.sumOf { it.hoursWorked?.toInt() ?: 0 }
        val overwork = workDays.sumOf { it.overtime?.toInt() ?: 0 }
        val underwork = workDays.sumOf { it.underwork?.toInt() ?: 0 }
        val latestVacation = repository.getLatestVacation(employeeId)
        val daysAvailable = 28 - repository.getVacationDaysAvailable(employeeId)

        return ShiftDetails(
            employee = employee,
            hours = hours,
            overwork = overwork,
            underwork = underwork,
            vacation = latestVacation,
            daysAvailable = daysAvailable
        )
    }
}
