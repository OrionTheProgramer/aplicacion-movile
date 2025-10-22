package am.gold.Navigation

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import am.gold.Screens.*
import am.gold.ViewModel.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val marketplaceViewModel: MarketplaceViewModel = viewModel(
        factory = MarketplaceViewModelFactory(application)
    )
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(application)
    )
    val cartViewModel: CartViewModel = viewModel(
        factory = CartViewModelFactory(application)
    )

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    if (isLoggedIn) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController = navController) }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppScreens.MarketplaceScreen.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(AppScreens.MarketplaceScreen.route) {
                    MarketplaceScreen(navController = navController, viewModel = marketplaceViewModel)
                }
                composable(AppScreens.CartScreen.route) {
                    CartScreen(navController = navController, cartViewModel = cartViewModel)
                }
                composable(AppScreens.BlogScreen.route) {
                    BlogScreen(navController = navController)
                }
                composable(AppScreens.SettingsScreen.route) {
                    SettingsScreen(navController = navController)
                }

                composable(AppScreens.EditProfileScreen.route) {
                    EditProfileScreen(navController = navController)
                }

                composable(
                    route = AppScreens.ProductDetailScreen.route,
                    arguments = listOf(navArgument("skinId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val skinId = backStackEntry.arguments?.getString("skinId")
                    if (skinId != null) {
                        ProductDetailScreen(
                            navController = navController,
                            marketplaceViewModel = marketplaceViewModel,
                            cartViewModel = cartViewModel,
                            skinId = skinId
                        )
                    } else {
                        Text("Error: ID de skin no válido.")
                    }
                }
                composable(AppScreens.CheckoutScreen.route) {
                    CheckoutScreen(navController = navController)
                }
                composable(
                    route = AppScreens.ReceiptScreen.route,
                    arguments = listOf(navArgument("totalAmount") { type = NavType.FloatType })
                ) { backStackEntry ->
                    val total = backStackEntry.arguments?.getFloat("totalAmount")?.toDouble()
                    ReceiptScreen(navController = navController, totalAmount = total)
                }
            }
        }
    } else {
        NavHost(
            navController = navController,
            startDestination = AppScreens.LoginScreen.route
        ) {
            composable(AppScreens.LoginScreen.route) {
                LoginScreen(navController = navController, authViewModel = authViewModel)
            }
            composable(AppScreens.RegisterScreen.route) {
                RegisterScreen(navController = navController, authViewModel = authViewModel)
            }
        }
    }
}