package ru.kubsu.market.core.model.repository

import ru.kubsu.market.core.model.WorkDay

interface TimeTrackingRepository {
    suspend fun isCheckedIn(userId: Int): Boolean
    suspend fun checkIn(userId: Int): WorkDay
    suspend fun checkOut(userId: Int): WorkDay
    suspend fun zeroOvertime(userId: Int): WorkDay
    suspend fun getWorkDays(employeeId: Int, startDate: String, endDate: String): List<WorkDay>
}
