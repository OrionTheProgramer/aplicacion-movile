package am.gold.Repository

import am.gold.Model.BlogPost
import am.gold.R

class BlogRepository {

    fun obtenerBlogPosts(): List<BlogPost> {
        return listOf(
            BlogPost(1, "Top 5 Skins de Phantom", "Las mejores skins para Phantom en 2025 según nuestra experiencia.", R.drawable.phantom_elderflame),
            BlogPost(2, "Guía rápida para dominar la Operator y obtener más eliminaciones.", "Descubre nuestras últimas novedades...", R.drawable.operator_prime),
            BlogPost(3, "Lanzamientos de skins esta semana", "Todas las novedades que llegan al juego esta semana.",R.drawable.vandal_reaver),
        )
    }
}