package ru.kubsu.market.feature.shift.domain.usecase

import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.feature.shift.domain.ShiftRepository

class RequestShiftVacationUseCase(
    private val repository: ShiftRepository
) {
    suspend operator fun invoke(vacation: Vacation) {
        repository.requestVacation(vacation)
    }
}
