package ru.kubsu.market.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int,
    val login: String,
    val role: String,
    val full_name: String?
)
