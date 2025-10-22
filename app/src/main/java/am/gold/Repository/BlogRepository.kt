package am.gold.Repository

import am.gold.Model.BlogPost

class BlogRepository {

    fun obtenerBlogPosts(): List<BlogPost> {
        return listOf(
            BlogPost(
                1,
                "Top 5 Skins de Phantom",
                "Las mejores skins para Phantom en 2025 según nuestra experiencia.",
                "valorant_skins/prime_phantom.png"
            ),

            BlogPost(
                2,
                "Guía rápida para dominar la Operator y obtener más eliminaciones.", "Descubre nuestras últimas novedades...",
                "valorant_skins/reaver_operator.png"
            ),

            BlogPost(
                3,
                "Lanzamientos de skins esta semana",
                "Todas las novedades que llegan al juego esta semana.",
                "valorant_skins/reaver_vandal.png"
            ),
        )
    }
}