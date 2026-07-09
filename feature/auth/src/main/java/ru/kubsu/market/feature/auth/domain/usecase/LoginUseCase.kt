package ru.kubsu.market.feature.auth.domain.usecase

import ru.kubsu.market.core.model.Token
import ru.kubsu.market.core.model.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(login: String, password: String): Token {
        return authRepository.login(login, password)
    }
}
