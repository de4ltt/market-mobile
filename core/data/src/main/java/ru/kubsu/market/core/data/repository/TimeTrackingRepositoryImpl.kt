package ru.kubsu.market.core.data.repository

import ru.kubsu.market.core.model.WorkDay
import ru.kubsu.market.core.model.repository.TimeTrackingRepository
import ru.kubsu.market.core.network.api.TimeTrackingApi
import ru.kubsu.market.core.network.mapper.toDomain
import javax.inject.Inject

class TimeTrackingRepositoryImpl @Inject constructor(
    private val timeTrackingApi: TimeTrackingApi
) : TimeTrackingRepository {

    override suspend fun isCheckedIn(userId: Int): Boolean {
        return timeTrackingApi.isCheckedIn(userId)
    }

    override suspend fun checkIn(userId: Int): WorkDay {
        return timeTrackingApi.checkIn(userId).toDomain()
    }

    override suspend fun checkOut(userId: Int): WorkDay {
        return timeTrackingApi.checkOut(userId).toDomain()
    }

    override suspend fun zeroOvertime(userId: Int): WorkDay {
        return timeTrackingApi.zeroOvertime(userId).toDomain()
    }

    override suspend fun getWorkDays(employeeId: Int, startDate: String, endDate: String): List<WorkDay> {
        return timeTrackingApi.getWorkDays(employeeId, startDate, endDate).map { it.toDomain() }
    }
}
