package ru.kubsu.market.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val access_token: String,
    val refresh_token: String,
    val token_type: String
)
