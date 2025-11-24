import am.gold.Util.ApiConfig

object ApiConfigMobile {
    // Ajusta a la IP pública del backend en EC2
    const PRODUCT_BASE = "http://35.170.88.63:8008"
    const CATALOGO_BASE = "http://35.170.88.63:8004"
    const AUTH_BASE = "http://35.170.88.63:8001"
    const CARRITO_BASE = "http://35.170.88.63:8002"
    const USUARIOS_BASE = "http://35.170.88.63:8003"
    const ORDENES_BASE = "http://35.170.88.63:8006"
    const PAGOS_BASE = "http://35.170.88.63:8007"

    fun productoImagenEndpoint(id: String) = "$PRODUCT_BASE/api/productos/$id/imagen"
}
