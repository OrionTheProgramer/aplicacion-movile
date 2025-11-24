package am.gold.Screens

import am.gold.Util.ApiConfigMobile
import am.gold.Util.getTierInfoFromUrl
import am.gold.ViewModel.MarketplaceViewModel
import am.gold.ViewModel.CartViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    marketplaceViewModel: MarketplaceViewModel,
    cartViewModel: CartViewModel,
    skinId: String
) {
    val skin = marketplaceViewModel.getSkinById(skinId)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Si no está en memoria, intenta recargar
    LaunchedEffect(skinId) {
        if (skin == null) marketplaceViewModel.refresh()
    }

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
                        cartViewModel.addToCart(skin)
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "${skin.name} añadido al carrito!",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "Añadir al carrito")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                val imageUri = if (skin.hasImageData) {
                    ApiConfigMobile.productoImagenEndpoint(skin.id)
                } else skin.imageUrl ?: skin.image

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = skin.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Fit
                )

                Text(skin.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tipo: ${skin.Type ?: skin.categoryName}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$${skin.price}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Categoría: ", style = MaterialTheme.typography.titleMedium)
                    val tierInfo = getTierInfoFromUrl(skin.Category ?: skin.categoryName)
                    Text(
                        text = tierInfo.name,
                        color = tierInfo.color,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text("Descripción", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = skin.desc ?: "Sin descripción",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 80.dp)
                )
            }
        }
    }
}
