package am.gold.Repository

import am.gold.Model.AuthResponse
import am.gold.Model.LoginRequest
import am.gold.Model.RegisterRequest
import am.gold.Util.ApiConfigMobile
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}

class AuthRepository {
    private val api: AuthApi = RetrofitProvider.build(ApiConfigMobile.AUTH_BASE, AuthApi::class.java)

    suspend fun login(email: String, password: String): AuthResponse =
        api.login(LoginRequest(correo = email, password = password))

    suspend fun register(username: String, email: String, password: String): AuthResponse =
        api.register(RegisterRequest(nombre = username, correo = email, password = password))
}
