package am.gold.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import android.content.Context
import am.gold.Model.BlogPost
import am.gold.repository.BlogRepository

class BlogViewModel(context: Context) : ViewModel() {

    private val repository = BlogRepository(context)

    var articulos by mutableStateOf(listOf<BlogPost>())
        private set

    init {
        articulos = repository.obtenerBlogPosts()
    }
}
