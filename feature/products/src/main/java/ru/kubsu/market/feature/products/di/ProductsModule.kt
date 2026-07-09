package ru.kubsu.market.feature.products.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kubsu.market.feature.products.data.repository.ProductsRepositoryImpl
import ru.kubsu.market.feature.products.domain.repository.ProductsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ProductsModule {

    @Binds
    @Singleton
    fun bindProductsRepository(
        impl: ProductsRepositoryImpl
    ): ProductsRepository
}
