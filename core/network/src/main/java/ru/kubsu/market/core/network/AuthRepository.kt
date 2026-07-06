package ru.kubsu.market.core.network

import ru.kubsu.market.core.network.dto.TokenResponse
import ru.kubsu.market.core.network.dto.UserResponse

interface AuthRepository {
    suspend fun login(login: String, password: String): TokenResponse
    suspend fun getMe(): UserResponse
    suspend fun logout()
    suspend fun refresh(refreshToken: String): TokenResponse
}
