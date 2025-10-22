package am.gold.Util

import androidx.compose.ui.graphics.Color

// Data class para guardar el nombre y color del tier
data class TierInfo(val name: String, val color: Color)

// Función que mapea la URL a la información del Tier
fun getTierInfoFromUrl(categoryUrl: String): TierInfo {
    return when (categoryUrl) {
        // --- AJUSTA ESTAS URLS A LAS QUE REALMENTE USAS ---
        "https://c-valorant-api.op.gg/Assets/ContentTiers/12683d76-48d7-84a3-4e09-6985794f0445.svg" -> TierInfo(
            "Select",
            Color(0xFF4CAF50)
        ) // Verde
        "https://c-valorant-api.op.gg/Assets/ContentTiers/0cebb8be-46d7-c12a-d306-e9907bfc5a25.svg" -> TierInfo(
            "Deluxe",
            Color(0xFF2196F3)
        ) // Azul
        "https://c-valorant-api.op.gg/Assets/ContentTiers/60BCA009-4182-7998-DEE7-B8A2558DC369.svg" -> TierInfo(
            "Premium",
            Color(0xFF9C27B0)
        ) // Morado
        "https://c-valorant-api.op.gg/Assets/ContentTiers/E046854E-406C-37F4-6607-19A9BA8426FC.svg" -> TierInfo(
            "Exclusive",
            Color(0xFFFF9800)
        ) // Naranja/Dorado
        "https://c-valorant-api.op.gg/Assets/ContentTiers/411E4A55-4E59-7757-41F0-86A53F101BB5.svg" -> TierInfo(
            "Ultra",
            Color(0xFFFFEB3B)
        ) // Amarillo
        else -> TierInfo("Desconocido", Color.Gray)
    }
}
