package am.gold.Screens

import am.gold.Navigation.AppScreens
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(navController: NavController, totalAmount: Double?) { // Recibe el total
    val context = LocalContext.current // Para usar Toast

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compra Exitosa") },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = "Éxito",
                tint = Color(0xFF4CAF50), // Verde
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "¡Gracias por tu compra!",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Tu pedido ha sido procesado exitosamente.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))

            // --- Detalles Simples de la Boleta ---
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resumen (Boleta Simulada)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    // TODO: Podrías pasar más detalles (ID de orden, fecha) si los tuvieras
                    ReceiptDetailRow("Monto Total:", "$${totalAmount?.formatPrice() ?: "N/A"}")
                    ReceiptDetailRow("Método de Pago:", "Simulado")
                    // ... otros detalles ...
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // --- Botones de Acción ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        // Simula descarga
                        Toast.makeText(context, "Descargando boleta (simulado)...", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Descargar")
                }
                Button(
                    onClick = {
                        navController.navigate(AppScreens.MarketplaceScreen.route) {
                            popUpTo(AppScreens.MarketplaceScreen.route) { inclusive = true } // Limpia todo hasta Marketplace
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary) // Botón dorado
                ) {
                    Text("Volver al Inicio")
                }
            }
        }
    }
}

// Helper para filas de detalle
@Composable
fun ReceiptDetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
        Text(value)
    }
}