package am.gold.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import am.gold.screens.BlogCard
import am.gold.viewmodel.BlogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogScreen(navController: NavController, blogViewModel: BlogViewModel = viewModel()) {
    val articulos = blogViewModel.articulos

    Scaffold(
        topBar = { TopAppBar(title = { Text("Blog Golden Rose") }) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(articulos) { articulo ->
                BlogCard(articulo = articulo, onClick = {
                    navController.navigate("blogDetail/${articulo.id}")
                })
            }
        }
    }
}

