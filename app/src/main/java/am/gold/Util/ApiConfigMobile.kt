package am.gold.Util

object ApiConfigMobile {
    // Ajusta a la IP publica del backend en EC2
    const val PRODUCT_BASE = "http://35.170.88.63:8008/"
    const val CATALOGO_BASE = "http://35.170.88.63:8004/"
    const val AUTH_BASE = "http://35.170.88.63:8001/"
    const val CARRITO_BASE = "http://35.170.88.63:8002/"
    const val USUARIOS_BASE = "http://35.170.88.63:8003/"
    const val ORDENES_BASE = "http://35.170.88.63:8006/"
    const val PAGOS_BASE = "http://35.170.88.63:8007/"

    fun productoImagenEndpoint(id: String) = "${PRODUCT_BASE.trimEnd('/')}/api/productos/$id/imagen"
}
