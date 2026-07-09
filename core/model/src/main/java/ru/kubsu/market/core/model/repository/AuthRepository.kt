package ru.kubsu.market.core.model.repository

import ru.kubsu.market.core.model.Employee
import ru.kubsu.market.core.model.Token
import ru.kubsu.market.core.model.User

interface AuthRepository {
    suspend fun login(login: String, password: String): Token
    suspend fun getMe(): User
    suspend fun logout()
    suspend fun refresh(refreshToken: String): Token
    suspend fun getEmployeeProfile(employeeId: Int): Employee
}
