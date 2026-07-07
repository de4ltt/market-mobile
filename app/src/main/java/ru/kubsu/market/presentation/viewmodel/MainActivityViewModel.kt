package ru.kubsu.market.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.core.data.SessionManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val sessionManager: SessionManager
) : ViewModel() {

    private val _showMidnightQuestion = MutableStateFlow(false)
    val showMidnightQuestion: StateFlow<Boolean> = _showMidnightQuestion.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.isCheckedIn.collect { checkedIn ->
                if (checkedIn) {
                    while (true) {
                        val delayMs = millisUntilNextMidnight(ZoneId.systemDefault())
                        delay(delayMs)
                        _showMidnightQuestion.value = true
                    }
                } else {
                    _showMidnightQuestion.value = false
                }
            }
        }
    }

    fun onZeroOvertimeChecked(userId: Int) {
        viewModelScope.launch {
            sessionManager.zeroOvertimeCheckOut(userId)
            _showMidnightQuestion.value = false
        }
    }

    fun dismissMidnightQuestion() {
        _showMidnightQuestion.value = false
    }

    private fun millisUntilNextMidnight(zoneId: ZoneId): Long {
        val now = LocalDateTime.now(zoneId)
        val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay(zoneId)
        return Duration.between(now.atZone(zoneId), nextMidnight).toMillis().coerceAtLeast(0)
    }
}
