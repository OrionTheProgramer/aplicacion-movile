package am.gold.Screens

import am.gold.Util.ApiConfigMobile
import am.gold.Util.getTierInfoFromUrl
import am.gold.ViewModel.CartViewModel
import am.gold.ViewModel.MarketplaceViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import am.gold.ui.components.GoldenRoseScreen
import am.gold.ui.components.GoldenSurfaceCard
import am.gold.ui.components.PillBadge
import am.gold.ui.components.PrimaryButton
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch

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

    LaunchedEffect(skinId) {
        if (skin == null) marketplaceViewModel.refresh()
    }

    GoldenRoseScreen(
        title = skin?.name ?: "Detalle de Skin",
        subtitle = "Revisa los detalles antes de comprar",
        onBack = { navController.popBackStack() },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        if (skin == null) {
            Text("Skin no encontrada", style = MaterialTheme.typography.titleLarge)
            return@GoldenRoseScreen
        }

        val imageUri = if (skin.hasImageData) {
            ApiConfigMobile.productoImagenEndpoint(skin.id)
        } else skin.imageUrl ?: skin.image

        GoldenSurfaceCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = skin.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    contentScale = ContentScale.Fit
                )

                val tierInfo = getTierInfoFromUrl(skin.Category ?: skin.categoryName ?: "")
                PillBadge(text = tierInfo.name, modifier = Modifier.align(Alignment.Start))

                Text(
                    text = skin.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tipo: ${skin.Type ?: skin.categoryName.orEmpty()}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "$${skin.price}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = skin.desc ?: "Esta skin no tiene descripcion aun.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(
                    text = "Agregar al carrito",
                    icon = Icons.Filled.ShoppingCart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    cartViewModel.addToCart(skin)
                    scope.launch {
                        snackbarHostState.showSnackbar("${skin.name} agregada al carrito")
                    }
                }
                TextButtonSecondary(
                    text = "Volver",
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun TextButtonSecondary(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    androidx.compose.material3.OutlinedButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(Icons.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp))
        Text(text)
    }
}
