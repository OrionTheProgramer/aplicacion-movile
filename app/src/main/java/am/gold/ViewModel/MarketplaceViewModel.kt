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


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery


    init {
        loadSkins()
    }

    private fun loadSkins() {
        viewModelScope.launch {
            _skins.value = repository.getSkins()
        }
    }

    suspend fun refresh() {
        _skins.value = repository.getSkins()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun getSkinById(skinId: String): Skin? {
        return _skins.value.find { it.id == skinId }
    }
}


// --- FÁBRICA DEL VIEWMODEL ---
// Esto es necesario para poder pasar el 'Application' al ViewModel
class MarketplaceViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarketplaceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MarketplaceViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
