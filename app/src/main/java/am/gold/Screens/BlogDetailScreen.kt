package am.gold.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import am.gold.Util.cargarImagenDeAssets
import am.gold.viewmodel.BlogViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogDetailScreen(blogId: Int?, navController: NavController, blogViewModel: BlogViewModel = viewModel()) {
    val articulo = blogViewModel.articulos.find { it.id == blogId } ?: return

    Scaffold(
        topBar = { TopAppBar(
            title = { Text("Detalle de la Noticia") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                }
            }
        ) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            val context = LocalContext.current
            val imageBitmap = cargarImagenDeAssets(context, articulo.imagenPath)

            Image(
                bitmap = imageBitmap,
                contentDescription = articulo.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = articulo.titulo,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = articulo.descripcion,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
