package am.gold.Screens

import am.gold.Navigation.AppScreens
import am.gold.Model.Skin
import am.gold.Util.ApiConfigMobile
import am.gold.Util.getTierInfoFromUrl
import am.gold.ViewModel.MarketplaceViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(navController: NavController, viewModel: MarketplaceViewModel) {

    val allSkins by viewModel.skins.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val filteredSkins = allSkins.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mercado de Skins") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            TextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                label = { Text("Buscar por nombre...") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                singleLine = true
            )

            if (allSkins.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredSkins) { skin ->
                    SkinCard(skin = skin, onSkinClick = {
                        navController.navigate(AppScreens.ProductDetailScreen.createRoute(skin.id))
                    })
                }
                if (filteredSkins.isEmpty() && searchQuery.isNotEmpty()) {
                    item {
                        Text(
                            "No se encontraron skins para \"$searchQuery\"",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkinCard(skin: Skin, onSkinClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSkinClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
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
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(skin.name, style = MaterialTheme.typography.titleMedium)
                Text(skin.categoryName ?: skin.Type.orEmpty(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = "$${skin.price}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                val tierInfo = getTierInfoFromUrl(skin.Category ?: skin.categoryName)

                Text(
                    text = tierInfo.name,
                    color = tierInfo.color,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
