package ru.kubsu.market.feature.receival.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import ru.kubsu.market.feature.receival.data.ReceivalRepositoryImpl
import ru.kubsu.market.feature.receival.domain.ReceivalRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReceivalModule {

    @Provides
    @Singleton
    fun provideReceivalRepository(
        httpClient: HttpClient
    ): ReceivalRepository {
        return ReceivalRepositoryImpl(httpClient)
    }
}
