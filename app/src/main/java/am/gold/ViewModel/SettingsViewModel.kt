// src/main/java/am/gold/ViewModel/SettingsViewModel.kt
package am.gold.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("GoldenRosePrefs", Context.MODE_PRIVATE)

    // --- Preferencias ---
    private val _username = MutableStateFlow<String>("")
    val username: StateFlow<String> = _username

    private val _receiveOffers = MutableStateFlow<Boolean>(false)
    val receiveOffers: StateFlow<Boolean> = _receiveOffers

    // Estado inicial del tema (ej. 'dark' o 'light' o 'system')
    private val _appTheme = MutableStateFlow<String>("dark") // Default a oscuro
    val appTheme: StateFlow<String> = _appTheme

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _username.value = sharedPreferences.getString("USER_NAME", "Usuario Golden") ?: "Usuario Golden" // Default username
        _receiveOffers.value = sharedPreferences.getBoolean("RECEIVE_OFFERS", false)
        _appTheme.value = sharedPreferences.getString("APP_THEME", "dark") ?: "dark"
    }

    // --- Funciones para actualizar ---
    fun updateUsername(newName: String) {
        // En una app real, esto iría a un backend
        _username.value = newName
        sharedPreferences.edit().putString("USER_NAME", newName).apply()
    }

    fun setReceiveOffers(enabled: Boolean) {
        _receiveOffers.value = enabled
        sharedPreferences.edit().putBoolean("RECEIVE_OFFERS", enabled).apply()
    }

    fun setAppTheme(theme: String) { // theme can be "light", "dark", "system"
        _appTheme.value = theme
        sharedPreferences.edit().putString("APP_THEME", theme).apply()
        // Aquí necesitarás una forma de que la UI principal reaccione a este cambio
        // Puede ser un StateFlow global o pasar este estado hacia arriba.
        // Por ahora, solo lo guardamos.
    }
}

// Factory para SettingsViewModel
class SettingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}