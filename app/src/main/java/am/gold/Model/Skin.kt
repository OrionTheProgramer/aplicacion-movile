package am.gold.Model

import com.google.gson.annotations.SerializedName

data class Skin(
    val id: String,
    val name: String,
    val price: Double,
    val Type: String,
    val Category: String,
    @SerializedName("image")
    val image: String,
    val desc: String
)

