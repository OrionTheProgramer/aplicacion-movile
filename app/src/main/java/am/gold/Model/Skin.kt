package am.gold.Model

import com.google.gson.annotations.SerializedName

data class Skin(
    @SerializedName("id")
    val id: String,
    @SerializedName("nombre")
    val name: String,
    @SerializedName("precio")
    val price: Double,

    // Categoria/nivel (backend usa categoriaNombre)
    @SerializedName("categoriaNombre")
    val categoryName: String? = null,

    // Imagen remota o embebida
    @SerializedName("imagenUrl")
    val imageUrl: String? = null,
    @SerializedName("hasImageData")
    val hasImageData: Boolean = false,

    // Descripcion
    @SerializedName("descripcion")
    val desc: String? = null,

    // Campos legacy para compatibilidad con assets locales
    @SerializedName("Type")
    val Type: String? = null,
    @SerializedName("Category")
    val Category: String? = null,
    @SerializedName("image")
    val image: String? = null
)
