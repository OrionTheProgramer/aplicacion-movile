package am.gold.Util

object ApiConfig {
    // Cambia 10.0.2.2 por la IP pública/privada del backend accesible desde el emulador/dispositivo
    const val PRODUCT_BASE = "http://10.0.2.2:8008"
    const val CATALOGO_BASE = "http://10.0.2.2:8004"

    fun productoImagenEndpoint(id: String) = "$PRODUCT_BASE/api/productos/$id/imagen"
}
