package ru.kubsu.market.feature.receival.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kubsu.market.feature.receival.data.ReceivalRepositoryImpl
import ru.kubsu.market.feature.receival.domain.ReceivalRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ReceivalModule {

    @Binds
    @Singleton
    fun bindReceivalRepository(
        impl: ReceivalRepositoryImpl
    ): ReceivalRepository
}
