package ru.kubsu.market.feature.products.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import ru.kubsu.market.feature.products.data.repository.ProductsRepositoryImpl
import ru.kubsu.market.feature.products.domain.repository.ProductsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductsModule {

    @Provides
    @Singleton
    fun provideProductsRepository(
        httpClient: HttpClient
    ): ProductsRepository {
        return ProductsRepositoryImpl(httpClient)
    }
}
