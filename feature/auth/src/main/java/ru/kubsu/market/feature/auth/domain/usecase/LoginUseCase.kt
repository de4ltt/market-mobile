package ru.kubsu.market.feature.auth.domain.usecase

import ru.kubsu.market.core.network.AuthRepository
import ru.kubsu.market.core.network.dto.TokenResponse

import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(login: String, password: String): TokenResponse {
        return authRepository.login(login, password)
    }
}
