package ru.kubsu.market.feature.employees.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kubsu.market.feature.employees.data.EmployeesRepositoryImpl
import ru.kubsu.market.feature.employees.data.ReportsRepositoryImpl
import ru.kubsu.market.feature.employees.domain.EmployeesRepository
import ru.kubsu.market.feature.employees.domain.ReportsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface EmployeesModule {

    @Binds
    @Singleton
    fun bindEmployeesRepository(
        impl: EmployeesRepositoryImpl
    ): EmployeesRepository

    @Binds
    @Singleton
    fun bindReportsRepository(
        impl: ReportsRepositoryImpl
    ): ReportsRepository
}
