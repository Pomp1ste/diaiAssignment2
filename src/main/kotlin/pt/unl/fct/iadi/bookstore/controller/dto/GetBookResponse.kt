package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size


data class GetBookResponse(
    @field:NotBlank
    @field:Schema(description = "unique ISBN")
    val isbn: String,

    @field:NotBlank
    @field:Size(min = 1, max = 120)
    @field:Schema(description = "title of 120 letters max")
    val title: String,

    @field:NotBlank
    @field:Size(min = 1, max = 80)
    @field:Schema(description = "Author of the book")
    val author: String,

    @field:NotBlank
    @field:Positive
    @field:Schema(description = "Price of the book")
    val price: Double,

    @field:NotBlank
    @field:Pattern(regexp = "^https://.*", message = "image must start with https://")
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