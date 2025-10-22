package am.gold.Navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import am.gold.Screens.* // Importa tus pantallas
import am.gold.ViewModel.MarketplaceViewModel
import am.gold.ViewModel.MarketplaceViewModelFactory
import androidx.compose.material3.Text

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Obtenemos el contexto y creamos la factory para el ViewModel
    val context = LocalContext.current
    val factory = MarketplaceViewModelFactory(context.applicationContext as Application)
    // Creamos una instancia del ViewModel que sobrevivirá en todas las pantallas
    val marketplaceViewModel: MarketplaceViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = AppScreens.MarketplaceScreen.route // Empezamos en el Mercado
    ) {

        // --- Pantalla del Mercado ---
        composable(AppScreens.MarketplaceScreen.route) {
            MarketplaceScreen(navController = navController, viewModel = marketplaceViewModel)
        }

        // --- Pantalla de Detalle de Producto ---
        composable(
            route = AppScreens.ProductDetailScreen.route,
            arguments = listOf(navArgument("skinId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Extrae el ID y lo pasa a la pantalla
            val skinId = backStackEntry.arguments?.getString("skinId")
            if (skinId != null) {
                ProductDetailScreen(
                    navController = navController,
                    viewModel = marketplaceViewModel,
                    skinId = skinId
                )
            }
        }

        // --- Pantallas (Placeholder) ---
        // Las definimos para que la app no crashee si navegas a ellas
        composable(AppScreens.LoginScreen.route) {
            Text("Pantalla de Login")
        }
        composable(AppScreens.RegisterScreen.route) {
            Text("Pantalla de Registro")
        }
        composable(AppScreens.CartScreen.route) {
            Text("Pantalla de Carrito")
        }
        composable(AppScreens.BlogScreen.route) {
            Text("Pantalla de Blog")
        }
    }
}