package pt.unl.fct.iadi.bookstore.domain

data class Book(
    val id: Long,
    val title: String,
    val author: String,
    //@field:Schema(description = "ISBN must have 13 digits", pattern = "^\\d{13}$")
    val isbn: String,
    //@field:Schema(description = "Price must be greater than zero", exclusiveMinimum = true, minimum = "0")
    val price: Double,
    val image: String
) 