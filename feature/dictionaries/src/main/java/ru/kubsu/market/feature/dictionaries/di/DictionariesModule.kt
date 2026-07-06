package ru.kubsu.market.feature.dictionaries.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import ru.kubsu.market.feature.dictionaries.data.DictionariesRepositoryImpl
import ru.kubsu.market.feature.dictionaries.domain.DictionariesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DictionariesModule {

    @Provides
    @Singleton
    fun provideDictionariesRepository(
        httpClient: HttpClient
    ): DictionariesRepository {
        return DictionariesRepositoryImpl(httpClient)
    }
}
