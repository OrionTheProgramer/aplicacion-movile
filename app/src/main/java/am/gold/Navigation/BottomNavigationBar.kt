// src/main/java/am/gold/Navigation/BottomNavigationBar.kt
package am.gold.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info // Placeholder para Blog
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront // Para Marketplace
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

// Define los items de la barra inferior
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem(AppScreens.MarketplaceScreen.route, Icons.Filled.Storefront, "Mercado") // Mercado como Home
    object Cart : BottomNavItem(AppScreens.CartScreen.route, Icons.Filled.ShoppingCart, "Carrito")
    object Blog : BottomNavItem(AppScreens.BlogScreen.route, Icons.Filled.Info, "Blog") // Icono placeholder
    object Settings : BottomNavItem(AppScreens.SettingsScreen.route, Icons.Filled.Settings, "Ajustes")
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cart,
        BottomNavItem.Blog,
        BottomNavItem.Settings
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Evita acumular historial si vuelves a presionar el mismo ítem
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Evita crear múltiples instancias de la misma pantalla
                        launchSingleTop = true
                        // Restaura el estado al volver
                        restoreState = true
                    }
                }
            )
        }
    }
}