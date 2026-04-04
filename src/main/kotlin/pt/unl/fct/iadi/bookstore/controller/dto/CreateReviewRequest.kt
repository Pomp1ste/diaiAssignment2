package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import pt.unl.fct.iadi.bookstore.domain.Review
import java.util.UUID

data class CreateReviewRequest(

    @field:Schema(description = "rating given")
    @field:Min(1)
    @field:Max(5)
    val rating: Int,

    @field:Schema(description = "comment")
    @field:Size(max = 500)
    val comment: String,

    @field:Schema(description = "Author of the book")
    val author: String
) {
    fun toReview(): Review {
        val review = Review(
            rating = rating,
            comment = comment,
            id = UUID.randomUUID(),
            author = author
        )
        return review
    }
}