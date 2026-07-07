package ru.kubsu.market.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kubsu.market.core.model.repository.AuthRepository
import ru.kubsu.market.core.model.repository.TimeTrackingRepository
import ru.kubsu.market.core.model.repository.VacationInfoRepository
import ru.kubsu.market.core.data.repository.AuthRepositoryImpl
import ru.kubsu.market.core.data.repository.TimeTrackingRepositoryImpl
import ru.kubsu.market.core.data.repository.VacationInfoRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    fun bindTimeTrackingRepository(
        impl: TimeTrackingRepositoryImpl
    ): TimeTrackingRepository

    @Binds
    @Singleton
    fun bindVacationInfoRepository(
        impl: VacationInfoRepositoryImpl
    ): VacationInfoRepository
}
