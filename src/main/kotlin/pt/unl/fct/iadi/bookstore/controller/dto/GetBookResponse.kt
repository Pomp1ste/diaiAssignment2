package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size


data class GetBookResponse(
    @field:Schema(description = "unique ISBN")
    val isbn: String,

    @field:Schema(description = "title of 120 letters max")
    val title: String,

    @field:Schema(description = "Author of the book")
    val author: String,


    @field:Schema(description = "Price of the book", minimum = "0")
    val price: Double,

    @field:Schema(description = "URL")
    val image: String

) {
    companion object {
        fun fromBook(book: pt.unl.fct.iadi.bookstore.domain.Book) = GetBookResponse(
            isbn = book.isbn,
            title = book.title,
            author = book.author,
            price = book.price,
            image = book.image
        )
    }
}