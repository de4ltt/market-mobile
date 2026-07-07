package ru.kubsu.market.core.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.kubsu.market.core.model.Role
import ru.kubsu.market.core.model.Vacation
import ru.kubsu.market.core.model.WorkDay
import ru.kubsu.market.core.model.repository.AuthRepository
import ru.kubsu.market.core.model.repository.TimeTrackingRepository
import ru.kubsu.market.core.model.repository.VacationInfoRepository
import ru.kubsu.market.core.network.UserPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

sealed interface SessionState {
    data object Loading : SessionState
    data object LoggedOut : SessionState
    data class LoggedIn(val userId: Int, val role: Role) : SessionState
}

@Singleton
class SessionManager @Inject constructor(
    private val userPrefs: UserPreferencesRepository,
    private val authRepository: AuthRepository,
    private val timeTrackingRepository: TimeTrackingRepository,
    private val vacationInfoRepository: VacationInfoRepository
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private val _isCheckedIn = MutableStateFlow(false)
    val isCheckedIn: StateFlow<Boolean> = _isCheckedIn.asStateFlow()

    private val _vacation = MutableStateFlow<Vacation?>(null)
    val vacation: StateFlow<Vacation?> = _vacation.asStateFlow()

    private val _vacationDays = MutableStateFlow<Long?>(null)
    val vacationDays: StateFlow<Long?> = _vacationDays.asStateFlow()

    init {
        scope.launch {
            userPrefs.accessToken.collectLatest { token ->
                if (token == null) {
                    _sessionState.value = SessionState.LoggedOut
                } else {
                    loadSession()
                }
            }
        }
    }

    private suspend fun loadSession() {
        _sessionState.value = SessionState.Loading
        try {
            val response = authRepository.getMe()
            val userId = response.id

            // Fetch vacation info
            _vacation.value = vacationInfoRepository.getVacation(userId)

            val currentYear = java.time.Year.now().value
            _vacationDays.value = 28 - vacationInfoRepository.getAvailableVacationDays(userId, currentYear)

            _isCheckedIn.value = timeTrackingRepository.isCheckedIn(userId)

            val role = Role.findByRole(response.role) ?: throw Exception("Invalid role")
            _sessionState.value = SessionState.LoggedIn(userId, role)
        } catch (e: Exception) {
            Log.e("SessionManager", "Error loading session", e)
            tryToRefresh()
        }
    }

    private suspend fun tryToRefresh() {
        val oldRefresh = userPrefs.refreshToken.firstOrNull()
        if (oldRefresh == null) {
            logout()
            return
        }
        try {
            val response = authRepository.refresh(oldRefresh)
            userPrefs.saveTokens(response.accessToken, response.refreshToken)
            loadSession()
        } catch (e: Exception) {
            Log.e("SessionManager", "Refresh failed", e)
            logout()
        }
    }

    suspend fun logout() {
        authRepository.logout()
        userPrefs.clearTokens()
        _vacation.value = null
        _vacationDays.value = null
        _isCheckedIn.value = false
        _sessionState.value = SessionState.LoggedOut
    }

    suspend fun checkIn(userId: Int): WorkDay {
        val result = timeTrackingRepository.checkIn(userId)
        _isCheckedIn.value = true
        return result
    }

    suspend fun checkOut(userId: Int): WorkDay {
        val result = timeTrackingRepository.checkOut(userId)
        _isCheckedIn.value = false
        return result
    }

    suspend fun zeroOvertimeCheckOut(userId: Int): WorkDay {
        val result = timeTrackingRepository.zeroOvertime(userId)
        _isCheckedIn.value = false
        return result
    }
}
