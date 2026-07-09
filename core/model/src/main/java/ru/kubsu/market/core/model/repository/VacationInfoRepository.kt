package ru.kubsu.market.core.model.repository

import ru.kubsu.market.core.model.Vacation

interface VacationInfoRepository {
    suspend fun getVacation(userId: Int): Vacation?
    suspend fun getAvailableVacationDays(userId: Int, year: Int): Long
    suspend fun requestVacation(vacation: Vacation): Vacation
}
