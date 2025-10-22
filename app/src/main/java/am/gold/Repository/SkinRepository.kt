package am.gold.Repository

import android.content.Context
import am.gold.Model.Skin
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class SkinRepository(private val context: Context) {

    fun getSkins(): List<Skin> {
        val jsonString: String
        try {
            // Usamos 'applicationContext' por seguridad
            jsonString = context.applicationContext.assets.open("skins.json")
                .bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return emptyList()
        }

        val listSkinType = object : TypeToken<List<Skin>>() {}.type
        return Gson().fromJson(jsonString, listSkinType)
    }
}