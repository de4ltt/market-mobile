package ru.kubsu.market.core.data.repository

import ru.kubsu.market.core.database.dao.VacationDao
import ru.kubsu.market.core.database.mapper.toEntity
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.model.repository.VacationInfoRepository
import ru.kubsu.market.core.network.api.VacationApi
import ru.kubsu.market.core.network.mapper.toDomain
import ru.kubsu.market.core.network.mapper.toDto
import javax.inject.Inject

class VacationInfoRepositoryImpl @Inject constructor(
    private val vacationApi: VacationApi,
    private val vacationDao: VacationDao
) : VacationInfoRepository {

    override suspend fun getVacation(userId: Int): Vacation? {
        return vacationApi.getVacations(userId).lastOrNull()?.toDomain()
    }

    override suspend fun getAvailableVacationDays(userId: Int, year: Int): Long {
        return vacationApi.getAvailableVacationDays(userId, year)
    }

    override suspend fun requestVacation(vacation: Vacation): Vacation {
        val createdDto = vacationApi.requestVacation(vacation.toDto())
        val created = createdDto.toDomain()
        vacationDao.insertVacation(created.toEntity())
        return created
    }
}
