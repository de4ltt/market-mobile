package ru.kubsu.market.core.data.repository

import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Token
import ru.kubsu.market.core.model.User
import ru.kubsu.market.core.model.repository.AuthRepository
import ru.kubsu.market.core.network.UserPreferencesRepository
import ru.kubsu.market.core.network.api.AuthApi
import ru.kubsu.market.core.network.dto.TokenResponse
import ru.kubsu.market.core.network.dto.UserResponse
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

    private fun TokenResponse.toDomain(): Token = Token(
        accessToken = access_token,
        refreshToken = refresh_token,
        tokenType = token_type
    )

    private fun UserResponse.toDomain(): User = User(
        id = id,
        login = login,
        role = role,
        fullName = full_name
    )
}
