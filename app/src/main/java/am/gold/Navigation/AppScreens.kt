package am.gold.Navigation

sealed class AppScreens(val route: String) {
    object LoginScreen : AppScreens("login_screen")
    object RegisterScreen : AppScreens("register_screen")
    object MarketplaceScreen : AppScreens("marketplace_screen")
    object ProductDetailScreen : AppScreens("product_detail_screen/{skinId}") {
        fun createRoute(skinId: String) = "product_detail_screen/$skinId"
    }
    object CartScreen : AppScreens("cart_screen")
    object BlogScreen : AppScreens("blog_screen")
    object SettingsScreen : AppScreens("settings_screen")
    object CheckoutScreen : AppScreens("checkout_screen")
    object ReceiptScreen : AppScreens("receipt_screen/{totalAmount}") {
        fun createRoute(total: Double) = "receipt_screen/$total"
    }
}