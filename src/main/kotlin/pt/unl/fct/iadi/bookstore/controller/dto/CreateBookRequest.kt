package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import pt.unl.fct.iadi.bookstore.domain.Book

data class CreateBookRequest (
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

    @field:Positive
    @field:Schema(description = "Price of the book")
    val price: Double,

    @field:NotBlank
    @field:Pattern(regexp = "^https://.*", message = "image must start with https://")
    @field:Schema(description = "URL")
    val image: String

) {
    fun toBook() = Book(
        author = author,
        price = price,
        image = image,
        isbn = isbn,
        title = title,
    )
}