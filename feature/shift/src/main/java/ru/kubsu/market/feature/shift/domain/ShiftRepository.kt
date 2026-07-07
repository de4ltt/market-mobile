package ru.kubsu.market.feature.shift.domain

import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.model.WorkDay

interface ShiftRepository {
    suspend fun getEmployeeProfile(employeeId: Int): Employee
    suspend fun getWorkDays(employeeId: Int, startDate: String, endDate: String): List<WorkDay>
    suspend fun requestVacation(vacation: Vacation)
    suspend fun getVacationDaysAvailable(employeeId: Int): Long
    suspend fun getLatestVacation(employeeId: Int): Vacation?
}
