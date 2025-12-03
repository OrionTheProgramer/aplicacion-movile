package am.gold.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import am.gold.Model.Skin
import am.gold.Repository.SkinRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MarketplaceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SkinRepository(application)
    private val _skins = MutableStateFlow<List<Skin>>(emptyList())
    val skins: StateFlow<List<Skin>> = _skins

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadSkins()
    }

    private fun loadSkins() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _skins.value = repository.getSkins()
            } catch (e: Exception) {
                _error.value = "No pudimos cargar el catalogo: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun refresh() {
        try {
            _isLoading.value = true
            _skins.value = repository.getSkins()
        } catch (e: Exception) {
            _error.value = "No pudimos actualizar el catalogo: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun getSkinById(skinId: String): Skin? {
        return _skins.value.find { it.id == skinId }
    }
}

// --- FABRICA DEL VIEWMODEL ---
class MarketplaceViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarketplaceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MarketplaceViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
