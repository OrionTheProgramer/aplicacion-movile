package am.gold.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogScreen(navController: NavController) { // Añadimos NavController
    Scaffold(topBar = { TopAppBar(title = { Text("Blog") }) }) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("Pantalla del Blog (Lista)")
            // Aquí iría tu LazyColumn leyendo blogs.json más adelante
        }
    }
}