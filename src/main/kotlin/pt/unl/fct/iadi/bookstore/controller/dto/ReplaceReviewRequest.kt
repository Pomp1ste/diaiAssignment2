package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import pt.unl.fct.iadi.bookstore.domain.Review
import java.util.UUID

data class ReplaceReviewRequest(
    @field:Schema(description = "rating given")
    @field:NotNull
    val rating: Int,

    @field:Schema(description = "isbn of the book related")
    @field:NotBlank
    val isbn: String,

    @field:Schema(description = "comment")
    val comment: String,

) {
    fun toReview(id: UUID): Review { return Review(id =  id, rating = rating, comment = comment) }
}