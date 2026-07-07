package ru.kubsu.market.feature.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.feature.auth.domain.usecase.LoginUseCase

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Error(val message: String) : AuthUiState
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(login: String, password: String, onSuccess: () -> Unit) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            try {
                loginUseCase(login, password)
                _uiState.value = AuthUiState.Idle
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Неверный логин или пароль")
            }
        }
    }
}

