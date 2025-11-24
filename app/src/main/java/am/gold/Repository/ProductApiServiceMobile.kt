package am.gold.Repository

import am.gold.Model.Skin
import am.gold.Util.ApiConfigMobile
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface ProductApiMobile {
    @GET("/api/productos")
    suspend fun getProducts(): List<Skin>

    @GET("/api/productos/{id}")
    suspend fun getProduct(@Path("id") id: String): Skin
}

object ProductApiServiceMobile {
    private fun buildRetrofit(baseUrl: String): ProductApiMobile {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiMobile::class.java)
    }

    val productoApi: ProductApiMobile by lazy { buildRetrofit(ApiConfigMobile.PRODUCT_BASE) }
    val catalogoApi: ProductApiMobile by lazy { buildRetrofit(ApiConfigMobile.CATALOGO_BASE) }
}
