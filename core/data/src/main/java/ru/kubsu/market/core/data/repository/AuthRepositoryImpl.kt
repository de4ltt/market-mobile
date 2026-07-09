package ru.kubsu.market.core.data.repository

import ru.kubsu.market.core.data.mapper.toDomain
import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Token
import ru.kubsu.market.core.model.User
import ru.kubsu.market.core.model.repository.AuthRepository
import ru.kubsu.market.core.network.UserPreferencesRepository
import ru.kubsu.market.core.network.api.AuthApi
import ru.kubsu.market.core.network.mapper.toDomain
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userPrefs: UserPreferencesRepository
) : AuthRepository {

    override suspend fun login(login: String, password: String): Token {
        val response = authApi.login(login, password)
        userPrefs.saveTokens(response.access_token, response.refresh_token)
        return response.toDomain()
    }

    override suspend fun getMe(): User {
        return authApi.getMe().toDomain()
    }

    override suspend fun logout() {
        userPrefs.clearTokens()
    }

    override suspend fun refresh(refreshToken: String): Token {
        val response = authApi.refresh(refreshToken)
        userPrefs.saveTokens(response.access_token, response.refresh_token)
        return response.toDomain()
    }

    override suspend fun getEmployeeProfile(employeeId: Int): Employee {
        return authApi.getEmployeeProfile(employeeId).toDomain()
    }
}
