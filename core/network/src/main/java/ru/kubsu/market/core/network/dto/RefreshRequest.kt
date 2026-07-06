package ru.kubsu.market.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    val refresh_token: String
)
