package ru.kubsu.market.core.data.mapper

import ru.kubsu.market.core.model.Token
import ru.kubsu.market.core.model.User
import ru.kubsu.market.core.network.dto.TokenResponse
import ru.kubsu.market.core.network.dto.UserResponse

fun TokenResponse.toDomain(): Token = Token(
    accessToken = access_token,
    refreshToken = refresh_token,
    tokenType = token_type
)

fun UserResponse.toDomain(): User = User(
    id = id,
    login = login,
    role = role,
    fullName = full_name
)
