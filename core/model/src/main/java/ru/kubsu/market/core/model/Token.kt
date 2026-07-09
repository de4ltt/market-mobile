package ru.kubsu.market.core.model

data class Token(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)
