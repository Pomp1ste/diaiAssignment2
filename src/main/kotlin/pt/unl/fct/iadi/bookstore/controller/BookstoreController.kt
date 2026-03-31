package pt.unl.fct.iadi.bookstore.controller

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.servlet.http.HttpServletRequest
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import pt.unl.fct.iadi.bookstore.controller.dto.CreateBookRequest
import pt.unl.fct.iadi.bookstore.controller.dto.GetBookResponse
import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.service.BookstoreService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import pt.unl.fct.iadi.bookstore.controller.dto.CreateReviewRequest
import pt.unl.fct.iadi.bookstore.controller.dto.PartialUpdateRequest
import pt.unl.fct.iadi.bookstore.controller.dto.ReplaceReviewRequest
import pt.unl.fct.iadi.bookstore.controller.dto.ReviewResponse
import pt.unl.fct.iadi.bookstore.controller.dto.UpdateReviewRequest
import pt.unl.fct.iadi.bookstore.domain.Review
import java.util.UUID

@RestController
class BookstoreController(
    private val service: BookstoreService,
    private val httpRequest: HttpServletRequest
): BookstoreAPI {

    @SecurityRequirement(name="apiToken")
    @SecurityRequirement(name="basicAuth")
    override fun createBook(request: CreateBookRequest): ResponseEntity<Unit> {

        val requestId = httpRequest.getHeader("X-Request-Id")
        println("Incoming requestId: $requestId")
        val book = service.createBook(request.toBook())
        val location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{isbn}")
            .buildAndExpand(book.isbn)
            .toUri()
        return ResponseEntity
            .created(location)
            .header("X-Request-Id", requestId ?: requestId)
            .build()
    }

    @SecurityRequirement(name="apiToken")
    override fun listBooks(): ResponseEntity<List<GetBookResponse>> {
        return ResponseEntity.ok(service.listBooks())
    }

    @SecurityRequirement(name="apiToken")
    override fun getBook(isbn: String): ResponseEntity<GetBookResponse> {
        return ResponseEntity.ok(GetBookResponse.fromBook(service.getBook(isbn)))
    }

    @SecurityRequirement(name="apiToken")
    @PreAuthorize("@bookstoreService.isAuthor(#isbn, authentication.name)")
    override fun putBook(isbn: String, request: CreateBookRequest): ResponseEntity<Unit> {
        val (book, created) = service.putBook(isbn = isbn, book =request.toBook())
        val requestId = httpRequest.getHeader("X-Request-Id")
        return if (created) {
            val location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{isbn}")
                .buildAndExpand(book.isbn)
                .toUri()
            ResponseEntity.created(location)
                .header("X-Request-Id", requestId ?: "")
                .build()
        } else {
            ResponseEntity.ok().build()
        }
    }

    @SecurityRequirement(name="apiToken")
    @PreAuthorize("@bookstoreService.isAuthor(#isbn, authentication.name)")
    override fun updateBook(isbn: String, request: PartialUpdateRequest): ResponseEntity<Unit> {
        service.updateBook(isbn = isbn, request)
        return ResponseEntity.ok().build()
    }

    @SecurityRequirement(name="apiToken")
    @PreAuthorize("@bookstoreService.isAuthor(#isbn, authentication.name) or hasRole('ADMIN')")
    override fun deleteBook(isbn: String): ResponseEntity<Unit> {
        service.deleteBook(isbn)
        print("Deleted \n")
        return ResponseEntity.noContent().build()
    }

    @SecurityRequirement(name="apiToken")
    override fun listReviews(isbn: String): ResponseEntity<List<ReviewResponse>> {
        return ResponseEntity.ok(service.listReviews(isbn))
    }

    @SecurityRequirement(name="apiToken")
    override fun createReview(isbn: String, request: CreateReviewRequest): ResponseEntity<Unit> {

        val requestId = httpRequest.getHeader("X-Request-Id")
        println("Incoming requestId: $requestId")
        val rev = request.toReview()
        service.createReview(isbn = request.isbn, review = rev)
        val location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(rev.id)
            .toUri()
        return ResponseEntity
            .created(location)
            .header("X-Request-Id", requestId ?: "")
            .build()
    }

    @SecurityRequirement(name="apiToken")
    override fun replaceReview(
        isbn: String,
        reviewId: UUID,
        request: ReplaceReviewRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(service.replaceReview(request.isbn, request.toReview(reviewId)))
    }

    @SecurityRequirement(name="apiToken")
    override fun updateReview(isbn: String, reviewId: UUID, request: UpdateReviewRequest): ResponseEntity<Unit> {
        val review: Review = Review(
            id = reviewId,
            rating = request.rating,
            comment = request.comment,
            author = ""
        )
        return ResponseEntity.ok(service.updateReview(isbn, reviewId, review))
    }

    @SecurityRequirement(name="apiToken")
    override fun deleteReview(isbn:String, reviewId: UUID): ResponseEntity<Unit> {
        return ResponseEntity.ok(service.deleteReview(reviewId))
    }
}