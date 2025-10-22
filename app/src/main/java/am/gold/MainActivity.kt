package am.gold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import am.gold.Navigation.AppScreens // Asegúrate de que esta ruta de importación coincida con la estructura de tu proyecto.
import am.gold.ui.theme.GoldenRoseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Esta función ayuda a que la app use toda la pantalla, dibujando detrás de las barras de sistema.
        enableEdgeToEdge()

        setContent {
            // GoldenRoseTheme aplica los estilos visuales (colores, fuentes) definidos en el paquete ui.theme.
            GoldenRoseTheme {
                // Surface es un contenedor que aplica el color de fondo correcto del tema.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // La llamada a AppNavigation() inicializa el sistema de navegación de tu app.
                    // Este componente decidirá qué pantalla (Login, Marketplace, etc.) se debe mostrar.
                    AppScreens()
                }
            }
        }
    }
}
