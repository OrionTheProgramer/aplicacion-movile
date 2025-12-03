package am.gold.Screens

import am.gold.Navigation.AppScreens
import am.gold.ViewModel.CartItem
import am.gold.ViewModel.CartViewModel
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import am.gold.ui.components.GoldenRoseScreen
import am.gold.ui.components.GoldenSurfaceCard
import am.gold.ui.components.PrimaryButton

@Composable
fun CartScreen(navController: NavController, cartViewModel: CartViewModel) {

    val cartItems by cartViewModel.cartItems.collectAsState()
    val subtotal = cartItems.sumOf { it.skin.price * it.quantity }
    val commission = if (subtotal > 0) subtotal * 0.05 else 0.0
    val shipping = if (subtotal > 0) 1490.0 else 0.0
    val total = subtotal + commission + shipping

    GoldenRoseScreen(
        title = "Tu carrito",
        subtitle = "Revisa y ajusta antes de pagar"
    ) {
        if (cartItems.isEmpty()) {
            GoldenSurfaceCard(
                title = "Carrito vacio",
                supportingText = "Agrega skins desde el marketplace."
            ) {
                PrimaryButton(
                    text = "Ir al Marketplace",
                    modifier = Modifier.fillMaxWidth()
                ) {
                    navController.navigate(AppScreens.MarketplaceScreen.route) {
                        popUpTo(AppScreens.MarketplaceScreen.route) { inclusive = true }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems) { item ->
                    CartItemRow(item = item, viewModel = cartViewModel)
                }
            }

            GoldenSurfaceCard(
                title = "Resumen",
                supportingText = "Valores incluyen comision de servicio y envio."
            ) {
                SummaryRow("Subtotal", subtotal)
                SummaryRow("Envio", shipping)
                SummaryRow("Comision", commission)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(
                        "$${total.formatPrice()}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                PrimaryButton(
                    text = "Continuar",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = cartItems.isNotEmpty()
                ) {
                    navController.navigate(AppScreens.CheckoutScreen.route)
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(item: CartItem, viewModel: CartViewModel) {
    GoldenSurfaceCard(
        title = item.skin.name,
        supportingText = "Cantidad: ${item.quantity}"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Subtotal: $${(item.skin.price * item.quantity).formatPrice()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = { viewModel.updateQuantity(item.skin.id, item.quantity - 1) },
                    modifier = Modifier.size(40.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                    enabled = item.quantity > 1
                ) {
                    Text("-")
                }
                Text(
                    "${item.quantity}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                OutlinedButton(
                    onClick = { viewModel.updateQuantity(item.skin.id, item.quantity + 1) },
                    modifier = Modifier.size(40.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Text("+")
                }
                IconButton(
                    onClick = { viewModel.removeFromCart(item.skin.id) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("$${value.formatPrice()}", style = MaterialTheme.typography.bodyMedium)
    }
}

private fun Double.formatPrice(): String = String.format("%.0f", this)
