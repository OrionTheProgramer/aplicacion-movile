package am.gold.Screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn // Si la lista puede ser larga
import androidx.compose.foundation.lazy.items // Si la lista puede ser larga
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import am.gold.Navigation.AppScreens // Para navegar a Receipt
import am.gold.ViewModel.* // Importa CartViewModel y Factory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController) {
    // Obtén el ViewModel del Carrito
    val context = LocalContext.current
    val factory = CartViewModelFactory(context.applicationContext as Application)
    val cartViewModel: CartViewModel = viewModel(factory = factory)
    val cartItems by cartViewModel.cartItems.collectAsState()

    // Calcula totales (igual que en CartScreen)
    val subtotal = cartItems.sumOf { it.skin.price * it.quantity }
    val commission = if (subtotal > 0) subtotal * 0.05 else 0.0
    val shipping = if (subtotal > 0) 1490.0 else 0.0 // Asegúrate que sea Double
    val total = subtotal + commission + shipping

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar Compra") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Botón para volver al carrito
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Resumen del Pedido", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            // Lista de items (simplificada)
            LazyColumn(modifier = Modifier.weight(1f)) { // Ocupa espacio disponible
                items(cartItems) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${item.skin.name} (x${item.quantity})")
                        Text("$${(item.skin.price * item.quantity).formatPrice()}")
                    }
                }
            }

            // Totales
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            SummaryRow("Subtotal:", subtotal)
            SummaryRow("Envío:", shipping)
            SummaryRow("Comisión:", commission)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total:", style = MaterialTheme.typography.titleLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text("$${total.formatPrice()}", style = MaterialTheme.typography.titleLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Pagar (Simulado)
            Button(
                onClick = {
                    // Acción de "pago":
                    // 1. Limpia el carrito
                    cartViewModel.clearCart() // Necesitas crear esta función en CartViewModel
                    // 2. Navega a la pantalla de recibo (pasando el total o ID de orden)
                    navController.navigate(AppScreens.ReceiptScreen.createRoute(total)) {
                        // Limpia el stack hasta Marketplace para que no pueda volver a Checkout
                        popUpTo(AppScreens.MarketplaceScreen.route)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = cartItems.isNotEmpty() // Deshabilita si el carrito está vacío
            ) {
                Text("Pagar")
            }
        }
    }
}

// Helper para formato de precio
fun Double.formatPrice(): String {
    // TODO: Implementar formato de moneda chilena si es necesario
    return String.format("%.0f", this) // Formato simple sin decimales
}

// Helper para filas de resumen
@Composable
fun SummaryRow(label: String, value: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("$${value.formatPrice()}", style = MaterialTheme.typography.bodyMedium)
    }
}