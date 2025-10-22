package am.gold.util

import am.gold.Model.BlogPost
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun cargarBlogsDesdeAssets(context: Context, fileName: String = "blogs.json"): List<BlogPost> {
    val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    val listType = object : TypeToken<List<BlogPost>>() {}.type
    return Gson().fromJson(jsonString, listType)
}
