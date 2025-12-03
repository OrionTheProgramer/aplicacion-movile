package am.gold.Repository

import am.gold.Model.CreateOrderRequest
import am.gold.Model.OrderResponse
import am.gold.Model.PaymentRequest
import am.gold.Model.PaymentResponse
import am.gold.Util.ApiConfigMobile
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OrderApi {
    @POST("/api/ordenes")
    suspend fun createOrder(
        @Header("Authorization") auth: String?,
        @Body request: CreateOrderRequest
    ): OrderResponse
}

interface PaymentApi {
    @POST("/api/pagos")
    suspend fun createPayment(
        @Header("Authorization") auth: String?,
        @Body request: PaymentRequest
    ): PaymentResponse
}

class OrderRepository {
    private val api: OrderApi = RetrofitProvider.build(ApiConfigMobile.ORDENES_BASE, OrderApi::class.java)

    suspend fun createOrder(request: CreateOrderRequest, token: String?): OrderResponse {
        val authHeader = token?.let { "Bearer $it" }
        return api.createOrder(authHeader, request)
    }
}

class PaymentRepository {
    private val api: PaymentApi = RetrofitProvider.build(ApiConfigMobile.PAGOS_BASE, PaymentApi::class.java)

    suspend fun createPayment(request: PaymentRequest, token: String?): PaymentResponse {
        val authHeader = token?.let { "Bearer $it" }
        return api.createPayment(authHeader, request)
    }
}
