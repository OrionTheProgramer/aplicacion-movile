package am.gold.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import am.gold.Navigation.AppScreens
import am.gold.ViewModel.AuthViewModel
import am.gold.ViewModel.CartViewModel
import am.gold.ViewModel.CheckoutState
import am.gold.ui.components.GoldenRoseScreen
import am.gold.ui.components.GoldenSurfaceCard
import am.gold.ui.components.PrimaryButton

@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel
) {

    val cartItems by cartViewModel.cartItems.collectAsState()
    val checkoutState by cartViewModel.checkoutState.collectAsState()
    val userId by authViewModel.userId.collectAsState()
    val token by authViewModel.authToken.collectAsState()

    val subtotal = cartItems.sumOf { it.skin.price * it.quantity }
    val commission = if (subtotal > 0) subtotal * 0.05 else 0.0
    val shipping = if (subtotal > 0) 1490.0 else 0.0
    val total = subtotal + commission + shipping

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(checkoutState) {
        when (checkoutState) {
            is CheckoutState.Error -> {
                val message = (checkoutState as CheckoutState.Error).message
                snackbarHostState.showSnackbar(message)
                cartViewModel.resetCheckoutState()
            }
            is CheckoutState.Success -> {
                cartViewModel.resetCheckoutState()
                navController.navigate(AppScreens.ReceiptScreen.createRoute(total)) {
                    popUpTo(AppScreens.MarketplaceScreen.route)
                }
            }
            else -> Unit
        }
    }

    GoldenRoseScreen(
        title = "Confirmar compra",
        subtitle = "Procesamos la orden contra microservicios de ordenes/pagos",
        onBack = { navController.popBackStack() },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        GoldenSurfaceCard(
            title = "Resumen del pedido",
            supportingText = "Revisa que todo este correcto antes de pagar."
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                items(cartItems) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${item.skin.name} (x${item.quantity})")
                        Text("$${(item.skin.price * item.quantity).formatPrice()}")
                    }
                }
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            SummaryRow("Subtotal", subtotal)
            SummaryRow("Envio", shipping)
            SummaryRow("Comision", commission)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", style = androidx.compose.material3.MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    "$${total.formatPrice()}",
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(
                text = when (checkoutState) {
                    CheckoutState.Loading -> "Procesando..."
                    else -> "Pagar y generar orden"
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = cartItems.isNotEmpty() && checkoutState != CheckoutState.Loading && token != null
            ) {
                cartViewModel.checkout(total, userId, token)
            }
            if (token == null) {
                Text(
                    "Debes iniciar sesion para enviar la orden al backend.",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun Double.formatPrice(): String = String.format("%.0f", this)

@Composable
private fun SummaryRow(label: String, value: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
        Text("$${value.formatPrice()}", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
    }
}
