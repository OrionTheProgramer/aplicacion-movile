package am.gold.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import am.gold.ViewModel.MarketplaceViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    viewModel: MarketplaceViewModel,
    skinId: String
) {
    // Buscar la skin en el ViewModel
    val skin = viewModel.getSkinById(skinId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(skin?.name ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Añadir al carrito */ }) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "Añadir al carrito")
            }
        }
    ) { paddingValues ->
        if (skin == null) {
            // Manejo de error si no se encuentra la skin
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Text("Skin no encontrada")
            }
        } else {
            // Contenido de la pantalla
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = skin.image,
                    contentDescription = skin.name,
                    modifier = Modifier.fillMaxWidth().height(250.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(skin.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tipo: ${skin.Type}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("$${skin.price}", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Descripción", style = MaterialTheme.typography.titleLarge)
                Text(skin.desc, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}