package am.gold.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import am.gold.Model.BlogPost
import am.gold.Repository.BlogRepository

class BlogViewModel : ViewModel() {

    private val repository = BlogRepository()

    var articulos by mutableStateOf(listOf<BlogPost>())
        private set

    init {
        cargarArticulos()
    }

    private fun cargarArticulos() {
        articulos = repository.obtenerBlogPosts()
    }
}
