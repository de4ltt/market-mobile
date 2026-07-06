package ru.kubsu.market.feature.auth.domain.usecase

import ru.kubsu.market.core.network.AuthRepository
import ru.kubsu.market.core.network.dto.TokenResponse

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(login: String, password: String): TokenResponse {
        return authRepository.login(login, password)
    }
}
