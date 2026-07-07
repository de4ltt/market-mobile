package ru.kubsu.market.feature.shift.domain.model

import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Vacation

data class ShiftDetails(
    val employee: Employee,
    val hours: Int,
    val overwork: Int,
    val underwork: Int,
    val vacation: Vacation?,
    val daysAvailable: Long
)
