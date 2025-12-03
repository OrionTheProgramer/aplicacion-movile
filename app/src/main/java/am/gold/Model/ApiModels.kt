package am.gold.Model

import com.google.gson.annotations.SerializedName

// AUTH
data class LoginRequest(
    @SerializedName("correo") val correo: String,
    @SerializedName("password") val password: String
)

data class RegisterRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("password") val password: String
)

data class AuthResponse(
    @SerializedName("token") val token: String? = null,
    @SerializedName("rol") val rol: String? = null,
    @SerializedName("usuarioId") val usuarioId: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("correo") val correo: String? = null,
    @SerializedName("email") val email: String? = null
)

// ORDENES / PAGOS
data class CartItemPayload(
    @SerializedName("productoId") val productoId: String,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("precioUnitario") val precioUnitario: Double
)

data class CreateOrderRequest(
    @SerializedName("usuarioId") val usuarioId: String?,
    @SerializedName("items") val items: List<CartItemPayload>,
    @SerializedName("total") val total: Double
)

data class OrderResponse(
    @SerializedName("id") val id: String? = null,
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("total") val total: Double? = null
)

data class PaymentRequest(
    @SerializedName("ordenId") val ordenId: String,
    @SerializedName("monto") val monto: Double,
    @SerializedName("metodo") val metodo: String = "tarjeta"
)

data class PaymentResponse(
    @SerializedName("id") val id: String? = null,
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("enlacePago") val enlacePago: String? = null
)
