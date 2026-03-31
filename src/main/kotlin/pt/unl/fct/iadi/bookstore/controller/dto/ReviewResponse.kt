package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import pt.unl.fct.iadi.bookstore.domain.Review
import java.util.UUID

data class ReviewResponse(
    @field:Schema(description = "Rating of the book")
    val rating: Int,

    @field:Schema(description = "Comment")
    val comment: String? = null,

    @field:Schema(description = "Author of review")
    val author: String,

    @field:Schema(description = "Review ID")
    var id: UUID
) {
    companion object {
        fun fromReview(review: Review): ReviewResponse = ReviewResponse(rating = review.rating!!, comment = review.comment, author = review.author!!, id = review.id)
        }
    }
