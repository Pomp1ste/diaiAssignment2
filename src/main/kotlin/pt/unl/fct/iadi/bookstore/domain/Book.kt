package pt.unl.fct.iadi.bookstore.domain

data class Book(
    val title: String,
    val author: String,
    val isbn: String,
    val price: Double,
    val image: String
) 