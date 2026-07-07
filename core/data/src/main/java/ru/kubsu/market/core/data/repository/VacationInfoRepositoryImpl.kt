package ru.kubsu.market.core.data.repository

import ru.kubsu.market.core.database.dao.VacationDao
import ru.kubsu.market.core.database.entity.VacationEntity
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.model.repository.VacationInfoRepository
import ru.kubsu.market.core.network.api.VacationApi
import ru.kubsu.market.core.network.dto.VacationDto
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
        val createdDto = vacationApi.requestVacation(VacationDto.fromDomain(vacation))
        val created = createdDto.toDomain()
        vacationDao.insertVacation(VacationEntity.fromDomain(created))
        return created
    }
}
