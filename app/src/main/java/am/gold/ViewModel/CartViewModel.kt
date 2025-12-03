package am.gold.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import am.gold.Model.Skin
import am.gold.Model.CartItemPayload
import am.gold.Model.CreateOrderRequest
import am.gold.Model.PaymentRequest
import am.gold.Repository.OrderRepository
import am.gold.Repository.PaymentRepository
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Anade 'quantity' al modelo Skin o crea un nuevo 'CartItem' data class
data class CartItem(val skin: Skin, var quantity: Int)

sealed class CheckoutState {
    object Idle : CheckoutState()
    object Loading : CheckoutState()
    data class Success(val orderId: String?, val paymentId: String?, val paymentLink: String?) : CheckoutState()
    data class Error(val message: String) : CheckoutState()
}

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("GoldenRosePrefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val orderRepository = OrderRepository()
    private val paymentRepository = PaymentRepository()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    val checkoutState: StateFlow<CheckoutState> = _checkoutState

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
            saveCart() // Guarda el carrito vacio
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

    fun checkout(total: Double, userId: String?, token: String?) {
        viewModelScope.launch {
            if (_cartItems.value.isEmpty()) return@launch
            if (token.isNullOrBlank()) {
                _checkoutState.value = CheckoutState.Error("Necesitas iniciar sesion para pagar.")
                return@launch
            }
            _checkoutState.value = CheckoutState.Loading

            try {
                val payload = _cartItems.value.map {
                    CartItemPayload(
                        productoId = it.skin.id,
                        cantidad = it.quantity,
                        precioUnitario = it.skin.price
                    )
                }
                val orderRequest = CreateOrderRequest(
                    usuarioId = userId,
                    items = payload,
                    total = total
                )
                val orderResponse = orderRepository.createOrder(orderRequest, token)

                val paymentResponse = paymentRepository.createPayment(
                    request = PaymentRequest(
                        ordenId = orderResponse.id ?: "orden_local",
                        monto = total
                    ),
                    token = token
                )

                _checkoutState.value = CheckoutState.Success(
                    orderId = orderResponse.id,
                    paymentId = paymentResponse.id,
                    paymentLink = paymentResponse.enlacePago
                )
                clearCart()
            } catch (e: Exception) {
                _checkoutState.value = CheckoutState.Error("No pudimos procesar tu compra: ${e.message}")
            }
        }
    }

    fun resetCheckoutState() {
        _checkoutState.value = CheckoutState.Idle
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
