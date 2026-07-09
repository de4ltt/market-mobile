package ru.kubsu.market.feature.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.feature.auth.domain.usecase.LoginUseCase
import ru.kubsu.market.feature.auth.presentation.model.AuthUiEvent
import ru.kubsu.market.feature.auth.presentation.model.AuthUiState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<AuthUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun login(login: String, password: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            try {
                loginUseCase(login, password)
                _uiState.value = AuthUiState.Idle
                _uiEvent.send(AuthUiEvent.LoginSuccess)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Неверный логин или пароль"
                _uiState.value = AuthUiState.Error(errorMessage)
                _uiEvent.send(AuthUiEvent.ShowToast(errorMessage))
            }
        }
    }
}
