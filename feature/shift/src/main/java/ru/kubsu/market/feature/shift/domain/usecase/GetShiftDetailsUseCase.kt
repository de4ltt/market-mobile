package ru.kubsu.market.feature.shift.domain.usecase

import java.time.LocalDate
import ru.kubsu.market.core.model.repository.AuthRepository
import ru.kubsu.market.core.model.repository.TimeTrackingRepository
import ru.kubsu.market.core.model.repository.VacationInfoRepository
import ru.kubsu.market.feature.shift.domain.model.ShiftDetails
import javax.inject.Inject

class GetShiftDetailsUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val timeTrackingRepository: TimeTrackingRepository,
    private val vacationInfoRepository: VacationInfoRepository
) {
    suspend operator fun invoke(employeeId: Int): ShiftDetails {
        val employee = authRepository.getEmployeeProfile(employeeId)
        val startDate = "${LocalDate.now().minusDays(LocalDate.now().dayOfWeek.value.toLong())}"
        val endDate = "${LocalDate.now()}"
        val workDays = timeTrackingRepository.getWorkDays(employeeId, startDate, endDate)
        val hours = workDays.sumOf { it.hoursWorked?.toInt() ?: 0 }
        val overwork = workDays.sumOf { it.overtime?.toInt() ?: 0 }
        val underwork = workDays.sumOf { it.underwork?.toInt() ?: 0 }
        val latestVacation = vacationInfoRepository.getVacation(employeeId)
        val daysAvailable = 28 - vacationInfoRepository.getAvailableVacationDays(employeeId, 2026)

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
