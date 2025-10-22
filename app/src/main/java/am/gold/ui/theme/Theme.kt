package am.gold.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ValorantRed,            // Color principal (botones, etc.)
    secondary = GoldenAccent,         // Color secundario (acentos)
    tertiary = GoldenAccent,          // Otro acento (puedes usar otro color)
    background = DarkBackground,      // Fondo general
    surface = DarkSurface,            // Fondo de tarjetas, barras
    surfaceVariant = DarkSurface,     // Un tono ligeramente diferente si quieres
    onPrimary = TextPrimary,          // Texto sobre botones primarios (rojos) -> Blanco
    onSecondary = Color.Black,        // Texto sobre botones secundarios (dorados) -> Negro
    onTertiary = Color.Black,         // Texto sobre botones terciarios (dorados) -> Negro
    onBackground = TextPrimary,       // Texto sobre el fondo general -> Blanco
    onSurface = TextPrimary,          // Texto sobre tarjetas/barras -> Blanco
    onSurfaceVariant = TextSecondary, // Texto secundario/hints -> Gris claro
    outline = BorderColor

)

// --- ESQUEMA CLARO (Déjalo como está por ahora o configúralo después) ---
private val LightColorScheme = lightColorScheme(
    primary = ValorantRed,
    secondary = GoldenAccent,
    tertiary = GoldenAccent,

)

@Composable
fun GoldenRoseTheme(
    darkTheme: Boolean = true, // Fuerza el tema oscuro
    dynamicColor: Boolean = true, // Desactiva colores dinámicos de Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}