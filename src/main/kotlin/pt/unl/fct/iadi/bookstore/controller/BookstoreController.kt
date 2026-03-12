package pt.unl.fct.iadi.bookstore.controller

import jakarta.servlet.http.HttpServletRequest
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity
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
import java.util.UUID

@RestController
class BookstoreController(
    private val service: BookstoreService,
    private val httpRequest: HttpServletRequest
): BookstoreAPI {

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

    override fun listBooks(): ResponseEntity<List<GetBookResponse>> {
        return ResponseEntity.ok(service.listBooks())
    }

    override fun getBook(isbn: String): ResponseEntity<GetBookResponse> {
        return ResponseEntity.ok(GetBookResponse.fromBook(service.getBook(isbn)))
    }

    override fun putBook(isbn: String, request: CreateBookRequest): ResponseEntity<Unit> {
        val (book, created) = service.putBook(isbn = isbn, book =request.toBook())
        val requestId = httpRequest.getHeader("X-Request-Id")
        return if (created) {
            val location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{isbn}")
                .buildAndExpand(book.isbn)
                .toUri()
            ResponseEntity.created(location)
                .header("X-Request-Id", requestId ?: requestId)
                .build()
        } else {
            ResponseEntity.ok().build()
        }
    }

    override fun updateBook(isbn: String, request: PartialUpdateRequest): ResponseEntity<Unit> {
        return ResponseEntity.ok(service.updateBook(isbn = isbn, request))
    }

    override fun deleteBook(isbn: String): ResponseEntity<Unit> {
        return ResponseEntity.ok(service.deleteBook(isbn))
    }

    override fun listReviews(isbn: String): ResponseEntity<List<ReviewResponse>> {
        return ResponseEntity.ok(service.listReviews(isbn))
    }

    override fun createReview(request: CreateReviewRequest): ResponseEntity<Unit> {

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
            .header("X-Request-Id", requestId ?: requestId)
            .build()
    }

    override fun replaceReview(
        id: UUID,
        request: ReplaceReviewRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(service.replaceReview(request.isbn, request.toReview(id)))
    }

    override fun deleteReview(reviewId: UUID): ResponseEntity<Unit> {
        return ResponseEntity.ok(service.deleteReview(reviewId))
    }
}