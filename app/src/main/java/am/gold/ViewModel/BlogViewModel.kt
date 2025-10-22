package am.gold.ViewModel

import am.gold.Model.BlogPost
import am.gold.Repository.BlogRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BlogViewModel : ViewModel() {

    private val repository = BlogRepository()
    private val _articulos = MutableLiveData<List<BlogPost>>()
    val articulos: LiveData<List<BlogPost>> get() = _articulos

    init {
        cargarArticulos()
    }
    private fun cargarArticulos() {
        _articulos.value = repository.obtenerBlogPosts()
    }

}