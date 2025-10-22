package am.gold.repository

import am.gold.Model.BlogPost
import android.content.Context
import am.gold.util.cargarBlogsDesdeAssets

class BlogRepository(private val context: Context) {

    fun obtenerBlogPosts(): List<BlogPost> {
        return cargarBlogsDesdeAssets(context)
    }
}
