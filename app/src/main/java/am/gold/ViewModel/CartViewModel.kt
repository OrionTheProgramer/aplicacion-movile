package am.gold.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import am.gold.Model.Skin
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Añade 'quantity' al modelo Skin o crea un nuevo 'CartItem' data class
data class CartItem(val skin: Skin, var quantity: Int)

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("GoldenRosePrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    init {
        loadCart()
    }

    private fun loadCart() {
        val jsonCart = sharedPreferences.getString("CART_ITEMS", null)
        if (jsonCart != null) {
            try {
                val type = object : TypeToken<List<CartItem>>() {}.type
                _cartItems.value = gson.fromJson(jsonCart, type) ?: emptyList()
            } catch (e: Exception) {
                _cartItems.value = emptyList() // Resetea si hay error al parsear
            }
        } else {
            _cartItems.value = emptyList()
        }
    }

    private fun saveCart() {
        val jsonCart = gson.toJson(_cartItems.value)
        sharedPreferences.edit().putString("CART_ITEMS", jsonCart).apply()
    }

    fun addToCart(skin: Skin) {
        viewModelScope.launch {
            val currentCart = _cartItems.value.toMutableList()
            val existingItem = currentCart.find { it.skin.id == skin.id }

            if (existingItem != null) {
                existingItem.quantity++
            } else {
                currentCart.add(CartItem(skin, 1))
            }
            _cartItems.value = currentCart
            saveCart()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            _cartItems.value = emptyList()
            saveCart() // Guarda el carrito vacío
            Log.d("CartViewModel", "clearCart: Carrito limpiado.")
        }
    }

    fun removeFromCart(skinId: String) {
        viewModelScope.launch {
            _cartItems.value = _cartItems.value.filterNot { it.skin.id == skinId }
            saveCart()
        }
    }

    fun updateQuantity(skinId: String, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity <= 0) {
                removeFromCart(skinId)
            } else {
                _cartItems.value = _cartItems.value.map {
                    if (it.skin.id == skinId) it.copy(quantity = newQuantity) else it
                }
                saveCart()
            }
        }
    }
}

class CartViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}