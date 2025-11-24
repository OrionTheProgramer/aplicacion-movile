package am.gold.navigation  // recuerda que los paquetes deben ir en minúsculas

import am.gold.Screens.BlogDetailScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import am.gold.Screens.BlogScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "blog"
    ) {
        composable("blog") {
            BlogScreen(navController)
        }

        composable("blogDetail/{blogId}") { backStackEntry ->
            val blogId = backStackEntry.arguments?.getString("blogId")?.toIntOrNull()
            BlogDetailScreen(blogId = blogId, navController = navController)
        }
    }
}
