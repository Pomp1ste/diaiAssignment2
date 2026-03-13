package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class UpdateReviewRequest (
    @field:Schema(description = "rating given")
    val rating: Int,

    @field:Schema(description = "isbn of the book related")
    @field:NotBlank
    val isbn: String,

    @field:Schema(description = "comment")
    val comment: String
)