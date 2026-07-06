package ru.kubsu.market.feature.shift.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import ru.kubsu.market.feature.shift.data.ShiftRepositoryImpl
import ru.kubsu.market.feature.shift.domain.ShiftRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ShiftModule {

    @Provides
    @Singleton
    fun provideShiftRepository(
        httpClient: HttpClient
    ): ShiftRepository {
        return ShiftRepositoryImpl(httpClient)
    }
}
