package am.gold.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import am.gold.Model.AuthResponse
import am.gold.Repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("GoldenRosePrefs", Context.MODE_PRIVATE)
    private val authRepository = AuthRepository()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> = _authToken

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        val role = sharedPreferences.getString("USER_ROLE", null)
        _isLoggedIn.value = role != null
        _userRole.value = role
        _userName.value = sharedPreferences.getString("USER_NAME", "") ?: ""
        _userEmail.value = sharedPreferences.getString("USER_EMAIL", "") ?: ""
        _userId.value = sharedPreferences.getString("USER_ID", null)
        _authToken.value = sharedPreferences.getString("AUTH_TOKEN", null)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            try {
                val response = authRepository.login(email, password)
                persistAuth(
                    response = response,
                    fallbackRole = "client",
                    fallbackUsername = email.substringBefore("@"),
                    fallbackEmail = email
                )
                _uiState.value = AuthUiState()
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = "No se pudo iniciar sesion: ${e.message}")
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            try {
                val response = authRepository.register(username, email, password)
                persistAuth(
                    response = response,
                    fallbackRole = "client",
                    fallbackUsername = username,
                    fallbackEmail = email
                )
                _uiState.value = AuthUiState()
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = "No se pudo registrar: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            with(sharedPreferences.edit()) {
                remove("USER_ROLE")
                remove("AUTH_TOKEN")
                remove("USER_NAME")
                remove("USER_EMAIL")
                remove("USER_ID")
                apply()
            }
            _isLoggedIn.value = false
            _userRole.value = null
            _userName.value = ""
            _userEmail.value = ""
            _userId.value = null
            _authToken.value = null
            _uiState.value = AuthUiState()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun persistAuth(
        response: AuthResponse,
        fallbackRole: String,
        fallbackUsername: String,
        fallbackEmail: String
    ) {
        val role = response.rol ?: fallbackRole
        val token = response.token ?: sharedPreferences.getString("AUTH_TOKEN", null) ?: ""
        val username = response.username ?: fallbackUsername
        val email = response.correo ?: response.email ?: fallbackEmail
        val userId = response.usuarioId

        with(sharedPreferences.edit()) {
            putString("USER_ROLE", role)
            putString("AUTH_TOKEN", token)
            putString("USER_NAME", username)
            putString("USER_EMAIL", email)
            userId?.let { putString("USER_ID", it) }
            apply()
        }

        _isLoggedIn.value = token.isNotBlank()
        _userRole.value = role
        _userName.value = username
        _userEmail.value = email
        _userId.value = userId
        _authToken.value = token
    }
}

class AuthViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
