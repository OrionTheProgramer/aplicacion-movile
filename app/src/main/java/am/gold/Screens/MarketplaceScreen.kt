package am.gold.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import am.gold.Model.Skin
import am.gold.Navigation.AppScreens
import am.gold.ViewModel.MarketplaceViewModel
import coil.compose.AsyncImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(navController: NavController, viewModel: MarketplaceViewModel) {

    val allSkins by viewModel.skins.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Lógica de filtrado
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
            // Barra de búsqueda
            TextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                label = { Text("Buscar por nombre...") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                singleLine = true
            )

            // Lista de Skins
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filteredSkins) { skin ->
                    SkinCard(skin = skin, onSkinClick = {
                        // Navega a la pantalla de detalle
                        navController.navigate(AppScreens.ProductDetailScreen.createRoute(skin.id))
                    })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkinCard(skin: Skin, onSkinClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onSkinClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = skin.image,
                contentDescription = skin.name,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(skin.name, style = MaterialTheme.typography.titleLarge)
                Text(skin.Type, style = MaterialTheme.typography.bodyMedium)
                Text("$${skin.price}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
