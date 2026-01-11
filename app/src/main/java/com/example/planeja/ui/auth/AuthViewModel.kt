package com.example.planeja.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planeja.domain.model.User
import com.example.planeja.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        restoreSession()
    }

    private fun restoreSession() {
        viewModelScope.launch {
            if (authRepository.isUserLoggedIn()) {
                _currentUser.value = authRepository.getCurrentUser()
            } else {
                _currentUser.value = null
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {

            if (!isValidEmail(email)) {
                _uiState.update { it.copy(error = "Email ou Senha Inválido") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            authRepository.login(email, password)
                .onSuccess { user ->
                    _currentUser.value = user
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
        }
    }

    fun register(email: String, password: String, name: String, onSuccess: () -> Unit) {
        viewModelScope.launch {

            when {
                name.isBlank() -> {
                    _uiState.update { it.copy(error = "Nome é obrigatório") }
                    return@launch
                }

                !isValidName(name) -> {
                    _uiState.update {
                        it.copy(error = "Informe nome e sobrenome sem números")
                    }
                    return@launch
                }

                !isValidEmail(email) -> {
                    _uiState.update { it.copy(error = "Email inválido") }
                    return@launch
                }

                !isValidPassword(password) -> {
                    _uiState.update {
                        it.copy(error = "Senha deve ter no mínimo 6 caracteres e 1 número")
                    }
                    return@launch
                }
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            authRepository.register(email, password, name)
                .onSuccess { user ->
                    _currentUser.value = user
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
        }
    }


    private fun isValidName(name: String): Boolean {
        val regex = Regex("^[A-Za-zÀ-ÿ]+(\\s[A-Za-zÀ-ÿ]+)+$")
        return regex.matches(name.trim())
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        val hasNumber = password.any { it.isDigit() }
        return password.length >= 6 && hasNumber
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _currentUser.value = null
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
