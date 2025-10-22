package am.gold.Screens

import am.gold.Navigation.AppScreens
import am.gold.ViewModel.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.room.Delete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, cartViewModel: CartViewModel) {

    val cartItems by cartViewModel.cartItems.collectAsState()
    val subtotal = cartItems.sumOf { it.skin.price * it.quantity }
    val commission = if (subtotal > 0) subtotal * 0.05 else 0.0
    val shipping = if (subtotal > 0) 1490.0 else 0.0
    val total = subtotal + commission + shipping

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Tu carrito está vacío", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { navController.navigate(AppScreens.MarketplaceScreen.route){ popUpTo(AppScreens.MarketplaceScreen.route){ inclusive = true } } }) {
                            Text("Ir al Mercado")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f), // Ocupa el espacio disponible
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Espacio entre items
                ) {
                    items(cartItems) { item ->
                        CartItemRow(item = item, viewModel = cartViewModel)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                SummaryRow("Subtotal:", subtotal)
                SummaryRow("Envío:", shipping)
                SummaryRow("Comisión:", commission)
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total:", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("$${total.formatPrice()}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate(AppScreens.CheckoutScreen.route) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = cartItems.isNotEmpty()
                ) {
                    Text("Continuar Compra")
                }
            }
        }
    }
}


@Composable
fun CartItemRow(item: CartItem, viewModel: CartViewModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Text(item.skin.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                Text("Cantidad: ${item.quantity}", style = MaterialTheme.typography.bodyMedium)
                Text("Subtotal: $${(item.skin.price * item.quantity).formatPrice()}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = { viewModel.updateQuantity(item.skin.id, item.quantity - 1) },
                    modifier = Modifier.size(40.dp),
                    contentPadding = PaddingValues(0.dp),
                    enabled = item.quantity > 1 // Deshabilita si la cantidad es 1
                ) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("${item.quantity}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = { viewModel.updateQuantity(item.skin.id, item.quantity + 1) },
                    modifier = Modifier.size(40.dp),
                    contentPadding = PaddingValues(0.dp),
                    // Podrías añadir un límite máximo si quieres
                ) {
                    Text("+")
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(
                    onClick = { viewModel.removeFromCart(item.skin.id) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}