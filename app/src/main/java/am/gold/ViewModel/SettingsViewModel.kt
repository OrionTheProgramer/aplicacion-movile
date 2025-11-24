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

    private val _username = MutableStateFlow<String>("")
    val username: StateFlow<String> = _username

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _bio = MutableStateFlow<String>("")
    val bio: StateFlow<String> = _bio

    private val _photoUri = MutableStateFlow<String?>(null)
    val photoUri: StateFlow<String?> = _photoUri

    private val _receiveOffers = MutableStateFlow<Boolean>(false)
    val receiveOffers: StateFlow<Boolean> = _receiveOffers

    private val _appTheme = MutableStateFlow<String>("dark")
    val appTheme: StateFlow<String> = _appTheme

    private val _pushNotificationsEnabled = MutableStateFlow<Boolean>(false)
    val pushNotificationsEnabled: StateFlow<Boolean> = _pushNotificationsEnabled

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _username.value = sharedPreferences.getString("USER_NAME", "Usuario Golden") ?: "Usuario Golden"
        _email.value = sharedPreferences.getString("USER_EMAIL", "") ?: ""
        _bio.value = sharedPreferences.getString("USER_BIO", "") ?: ""
        _photoUri.value = sharedPreferences.getString("USER_PHOTO", null)
        _receiveOffers.value = sharedPreferences.getBoolean("RECEIVE_OFFERS", false)
        _appTheme.value = sharedPreferences.getString("APP_THEME", "dark") ?: "dark"
        _pushNotificationsEnabled.value = sharedPreferences.getBoolean("PUSH_NOTIFICATIONS", false)
    }

    fun updateUsername(newName: String) {
        _username.value = newName
        sharedPreferences.edit().putString("USER_NAME", newName).apply()
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
        sharedPreferences.edit().putString("USER_EMAIL", newEmail).apply()
    }

    fun updateBio(newBio: String) {
        _bio.value = newBio
        sharedPreferences.edit().putString("USER_BIO", newBio).apply()
    }

    fun updatePhoto(uri: String?) {
        _photoUri.value = uri
        sharedPreferences.edit().putString("USER_PHOTO", uri).apply()
    }

    fun updateProfile(name: String, email: String, bio: String, photo: String?) {
        _username.value = name
        _email.value = email
        _bio.value = bio
        _photoUri.value = photo
        sharedPreferences.edit()
            .putString("USER_NAME", name)
            .putString("USER_EMAIL", email)
            .putString("USER_BIO", bio)
            .putString("USER_PHOTO", photo)
            .apply()
    }

    fun setReceiveOffers(enabled: Boolean) {
        _receiveOffers.value = enabled
        sharedPreferences.edit().putBoolean("RECEIVE_OFFERS", enabled).apply()
    }

    fun setAppTheme(theme: String) {
        _appTheme.value = theme
        sharedPreferences.edit().putString("APP_THEME", theme).apply()
    }

    fun setPushNotificationsEnabled(enabled: Boolean) {
        _pushNotificationsEnabled.value = enabled
        sharedPreferences.edit().putBoolean("PUSH_NOTIFICATIONS", enabled).apply()
    }
}

class SettingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
