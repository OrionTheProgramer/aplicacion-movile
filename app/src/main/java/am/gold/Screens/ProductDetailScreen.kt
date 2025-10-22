package am.gold.Screens

import android.util.Log // Asegúrate de importar Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember // Para Snackbar
import androidx.compose.runtime.rememberCoroutineScope // Para Snackbar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Importa Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight // Para negrita
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import am.gold.Util.getTierInfoFromUrl // Importa la función auxiliar
import am.gold.ViewModel.MarketplaceViewModel
import am.gold.ViewModel.CartViewModel // Importa CartViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch // Para Snackbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    marketplaceViewModel: MarketplaceViewModel, // ViewModel para obtener datos de la skin
    cartViewModel: CartViewModel,           // ViewModel para añadir al carrito
    skinId: String
) {
    val skin = marketplaceViewModel.getSkinById(skinId)
    val snackbarHostState = remember { SnackbarHostState() } // Estado para Snackbar
    val scope = rememberCoroutineScope() // CoroutineScope para lanzar Snackbar

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(skin?.name ?: "Detalle de Skin") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (skin != null) {
                        cartViewModel.addToCart(skin) // Llama a la función del ViewModel
                        // Muestra confirmación
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "${skin.name} añadido al carrito!",
                                duration = SnackbarDuration.Short // Duración corta
                            )
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "Añadir al carrito")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) } // Host para mostrar Snackbar
    ) { paddingValues ->
        if (skin == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Skin no encontrada", style = MaterialTheme.typography.titleLarge)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Padding del Scaffold
                    .padding(horizontal = 16.dp) // Padding lateral
                    .verticalScroll(rememberScrollState()) // Habilita scroll
            ) {
                Spacer(modifier = Modifier.height(16.dp)) // Espacio superior

                // Imagen Principal (desde Assets)
                val imageUri = "file:///android_asset/${skin.image}"
                Log.d("ImageDebug", "Cargando imagen DETALLE desde: $imageUri") // Log para depurar ruta
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        // .placeholder(R.drawable.placeholder_image) // Opcional
                        // .error(R.drawable.error_image) // Opcional
                        .build(),
                    contentDescription = skin.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp) // Altura de la imagen
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Fit // Ajusta sin cortar
                )

                // Detalles de la Skin
                Text(skin.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tipo: ${skin.Type}", style = MaterialTheme.typography.titleMedium) // Asegúrate que 'Type' coincida con Skin.kt
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${skin.price}", // Formato simple de precio
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Categoría con Texto Coloreado
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Categoría: ", style = MaterialTheme.typography.titleMedium)
                    val tierInfo = getTierInfoFromUrl(skin.Category) // Llama a la función auxiliar
                    Text(
                        text = tierInfo.name,
                        color = tierInfo.color, // Aplica el color del tier
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Descripción
                Text("Descripción", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = skin.desc, // Asegúrate que 'desc' coincida con Skin.kt
                    style = MaterialTheme.typography.bodyLarge,
                    // Padding inferior importante para que el FAB no tape el texto
                    modifier = Modifier.padding(bottom = 80.dp)
                )
            }
        }
    }
}