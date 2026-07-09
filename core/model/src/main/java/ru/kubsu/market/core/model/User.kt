package ru.kubsu.market.core.model

data class User(
    val id: Int,
    val login: String,
    val role: String,
    val fullName: String?
)
