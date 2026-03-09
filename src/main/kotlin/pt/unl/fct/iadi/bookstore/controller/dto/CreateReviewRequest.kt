package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import pt.unl.fct.iadi.bookstore.domain.Review
import java.util.UUID

data class CreateReviewRequest(
    @field:Schema(description = "unique ISBN")
    @field:NotBlank
    val isbn: String,

    @field:Schema(description = "rating given")
    @field:NotBlank
    val rating: Int,

    @field:Schema(description = "comment")
    val comment: String
) {
    fun toReview(): Review {
        val review = Review(
            rating = rating,
            comment = comment,
            id = UUID.randomUUID()

        )
        return review
    }
}