package am.gold.Screens

import am.gold.Navigation.AppScreens
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import am.gold.ui.components.GoldenRoseScreen
import am.gold.ui.components.GoldenSurfaceCard

@Composable
fun ReceiptScreen(navController: NavController, totalAmount: Double?) {
    val context = LocalContext.current

    GoldenRoseScreen(
        title = "Compra exitosa",
        subtitle = "Guardamos el comprobante en tu cuenta"
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = "Exito",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                "Gracias por tu compra",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Text(
                "Tu pedido ha sido procesado y se envio al microservicio de ordenes/pagos.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            GoldenSurfaceCard(
                title = "Resumen",
                supportingText = "Puedes consultar la orden en tu historial."
            ) {
                ReceiptDetailRow("Monto total", "$${totalAmount?.formatPrice() ?: "N/A"}")
                ReceiptDetailRow("Metodo de pago", "Integracion Golden Rose")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        Toast.makeText(context, "Descargando boleta (simulado)...", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(Modifier.padding(horizontal = 4.dp))
                    Text("Descargar")
                }
                Button(
                    onClick = {
                        navController.navigate(AppScreens.MarketplaceScreen.route) {
                            popUpTo(AppScreens.MarketplaceScreen.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Volver al inicio")
                }
            }
        }
    }
}

@Composable
fun ReceiptDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Medium)
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}

private fun Double.formatPrice(): String = String.format("%.0f", this)
