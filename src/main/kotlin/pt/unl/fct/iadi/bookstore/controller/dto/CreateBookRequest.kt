package pt.unl.fct.iadi.bookstore.controller.dto

data class CreateBookRequest (
    @field:NotBlank
    @field:Schema(description = "unique ISBN")
    val isbn: String,
)