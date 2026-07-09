package ru.kubsu.market.feature.dictionaries.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kubsu.market.feature.dictionaries.data.DictionariesRepositoryImpl
import ru.kubsu.market.feature.dictionaries.domain.DictionariesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DictionariesModule {

    @Binds
    @Singleton
    fun bindDictionariesRepository(
        impl: DictionariesRepositoryImpl
    ): DictionariesRepository
}
