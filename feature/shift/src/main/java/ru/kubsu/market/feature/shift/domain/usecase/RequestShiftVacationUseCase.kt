package ru.kubsu.market.feature.shift.domain.usecase

import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.model.repository.VacationInfoRepository
import javax.inject.Inject

class RequestShiftVacationUseCase @Inject constructor(
    private val vacationInfoRepository: VacationInfoRepository
) {
    suspend operator fun invoke(vacation: Vacation) {
        vacationInfoRepository.requestVacation(vacation)
    }
}
