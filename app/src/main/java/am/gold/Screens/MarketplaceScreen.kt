package am.gold.Screens

import am.gold.Model.Skin
import am.gold.Navigation.AppScreens
import am.gold.Util.ApiConfigMobile
import am.gold.Util.getTierInfoFromUrl
import am.gold.ViewModel.MarketplaceViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import am.gold.ui.components.PillBadge
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch

@Composable
fun MarketplaceScreen(navController: NavController, viewModel: MarketplaceViewModel) {
    val allSkins by viewModel.skins.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(error) {
        error?.let { message ->
            scope.launch { snackbarHostState.showSnackbar(message) }
        }
    }

    val filteredSkins = allSkins.filter { it.name.contains(searchQuery, ignoreCase = true) }

    GoldenRoseScreen(
        title = "Marketplace",
        subtitle = "Claves de skins listas para usar",
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            label = { Text("Buscar por nombre") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )

        if (isLoading && allSkins.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filteredSkins) { skin ->
                SkinCard(
                    skin = skin,
                    onSkinClick = { navController.navigate(AppScreens.ProductDetailScreen.createRoute(skin.id)) }
                )
            }

            if (filteredSkins.isEmpty() && searchQuery.isNotEmpty() && !isLoading) {
                item {
                    Text(
                        "No encontramos resultados para \"$searchQuery\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SkinCard(skin: Skin, onSkinClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSkinClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val imageUri = if (skin.hasImageData) {
                ApiConfigMobile.productoImagenEndpoint(skin.id)
            } else skin.imageUrl ?: skin.image

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = skin.name,
                modifier = Modifier.size(96.dp),
                contentScale = ContentScale.Fit
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(skin.name, style = MaterialTheme.typography.titleLarge)
                Text(
                    skin.categoryName ?: skin.Type.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${skin.price}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                val tierInfo = getTierInfoFromUrl(skin.Category ?: skin.categoryName)
                Spacer(modifier = Modifier.height(6.dp))
                PillBadge(text = tierInfo.name)
            }
        }
    }
}
