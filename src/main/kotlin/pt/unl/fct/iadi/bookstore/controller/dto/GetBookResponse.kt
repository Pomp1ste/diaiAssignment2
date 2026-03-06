package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank


data class GetBookResponse(
    @field:NotBlank
    @field:Schema(description = "unique ISBN")
    val isbn: String,

    @field:NotBlank
    @field:Schema(description = "title of 120 letters max")
    val title: String,

    @field:NotBlank
    @field:Schema(description = "Author of the book")
    val author: String,

    @field:NotBlank
    @field:Schema(description = "Price of the book")
    val price: Double,

    @field:NotBlank
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