package pt.unl.fct.iadi.bookstore.controller.dto

import jakarta.validation.constraints.NotBlank

data class GetBookRequest (
    @field:NotBlank
    val isbn: String
)