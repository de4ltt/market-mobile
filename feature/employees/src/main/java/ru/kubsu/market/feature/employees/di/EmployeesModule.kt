package ru.kubsu.market.feature.employees.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import ru.kubsu.market.core.database.dao.EmployeeDao
import ru.kubsu.market.core.database.dao.VacationDao
import ru.kubsu.market.feature.employees.data.EmployeesRepositoryImpl
import ru.kubsu.market.feature.employees.domain.EmployeesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EmployeesModule {

    @Provides
    @Singleton
    fun provideEmployeesRepository(
        httpClient: HttpClient,
        employeeDao: EmployeeDao,
        vacationDao: VacationDao
    ): EmployeesRepository {
        return EmployeesRepositoryImpl(httpClient, employeeDao, vacationDao)
    }
}
