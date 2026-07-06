package ru.kubsu.market.core.network.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import ru.kubsu.market.core.network.AuthRepository
import ru.kubsu.market.core.network.AuthRepositoryImpl
import ru.kubsu.market.core.network.HttpClientProvider
import ru.kubsu.market.core.network.UserPreferencesRepository
import ru.kubsu.market.core.network.userDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        @ApplicationContext context: Context
    ): UserPreferencesRepository {
        return UserPreferencesRepository(context.userDataStore)
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        userPreferencesRepository: UserPreferencesRepository
    ): HttpClient {
        return HttpClientProvider(userPreferencesRepository).create()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        httpClient: HttpClient,
        userPreferencesRepository: UserPreferencesRepository
    ): AuthRepository {
        return AuthRepositoryImpl(httpClient, userPreferencesRepository)
    }
}
