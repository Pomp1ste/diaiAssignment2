package pt.unl.fct.iadi.bookstore.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import pt.unl.fct.iadi.bookstore.controller.dto.CreateBookRequest
import pt.unl.fct.iadi.bookstore.controller.dto.CreateReviewRequest
import pt.unl.fct.iadi.bookstore.controller.dto.GetBookResponse
import pt.unl.fct.iadi.bookstore.controller.dto.PartialUpdateRequest
import pt.unl.fct.iadi.bookstore.controller.dto.ReplaceReviewRequest
import pt.unl.fct.iadi.bookstore.controller.dto.ReviewResponse
import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.service.AlreadyExists
import pt.unl.fct.iadi.bookstore.service.BookNotFoundException
import pt.unl.fct.iadi.bookstore.service.ReviewNotFoundException
import java.util.UUID

@Tag(name = "Bookstore API", description = "Bookstore API")
interface BookstoreAPI {
    @Operation(summary = "Create a new book", operationId = "createBook")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Book created",
            headers = [Header(
                name = "Location", description = "URI of the created book",
                schema = Schema(type = "string", format = "uri")
            )],
            content = [Content(schema = Schema(hidden = true))]),
        ApiResponse(responseCode = "400", description = "Validation error",
            content = [Content(schema = Schema(implementation = MethodArgumentNotValidException::class))]),
        ApiResponse(responseCode = "409", description = "Book with this ISBN already exists",
            content = [Content(schema = Schema(implementation = AlreadyExists::class))])
    )
    @RequestMapping(
        value = ["/books"],
        consumes = ["application/json"],
        method = [RequestMethod.POST]
    )
    fun createBook(@Valid @RequestBody request: CreateBookRequest): ResponseEntity<Unit>

    //#####################################################

    @Operation(summary = "List all books", operationId = "listBooks")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "List of all books")
    )
    @RequestMapping(
        value = ["/books"],
        produces = ["application/json"],
        method = [RequestMethod.GET]
    )
    fun listBooks(): ResponseEntity<List<GetBookResponse>>

    //#####################################################

    @Operation(summary = "Get a single book", operationId = "getBook")
    @ApiResponses(
        ApiResponse(responseCode = "400", description = "Validation error",
            content = [Content(schema = Schema(implementation = MethodArgumentNotValidException::class))]),
        ApiResponse(responseCode = "404", description = "Book not found",
            content = [Content(schema = Schema(implementation = BookNotFoundException::class))]),
        ApiResponse(responseCode = "200", description = "Book fetched",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = GetBookResponse::class))])
    )
    @RequestMapping(
        value = ["/books/{isbn}"],
        produces = ["application/json"],
        method = [RequestMethod.GET]
    )
    fun getBook(@PathVariable isbn: String): ResponseEntity<GetBookResponse>

    //#####################################################

    @Operation(summary = "Replace a book", operationId = "replaceBook")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Book replaced",
            headers = [Header(
                name = "Location", description = "URI of the replaced book",
                schema = Schema(type = "string", format = "uri")
            )],
            content = [Content(schema = Schema(hidden = true))]),
        ApiResponse(responseCode = "201", description = "Book created",
            headers = [Header(
                name = "Location", description = "URI of the created book",
                schema = Schema(type = "string", format = "uri")
            )],
            content = [Content(schema = Schema(hidden = true))]),
        ApiResponse(responseCode = "400", description = "Validation error",
            content = [Content(schema = Schema(implementation = MethodArgumentNotValidException::class))]),
    )
    @RequestMapping(
        value = ["/books/{isbn}"],
        consumes = ["application/json"],
        method = [RequestMethod.PUT]
    )
    fun putBook(@PathVariable isbn: String, @Valid @RequestBody request: CreateBookRequest): ResponseEntity<Unit>


    //#####################################################

    @Operation(summary = "Partially update a book", operationId = "PartialUpdateBook")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Book updated",
            headers = [Header(
                name = "Location", description = "URI of the updated book",
                schema = Schema(type = "string", format = "uri")
            )],
            content = [Content(schema = Schema(hidden = true))]),
        ApiResponse(responseCode = "400", description = "Validation error",
            content = [Content(schema = Schema(implementation = MethodArgumentNotValidException::class))]),
        ApiResponse(responseCode = "404", description = "Book not found",
            content = [Content(schema = Schema(implementation = BookNotFoundException::class))])
    )
    @RequestMapping(
        value = ["/books/{isbn}"],
        consumes = ["application/json"],
        method = [RequestMethod.PATCH]
    )
    fun updateBook(@PathVariable isbn: String, @Valid @RequestBody request: PartialUpdateRequest): ResponseEntity<Unit>

    //#####################################################

    @Operation(summary = "Delete a book", operationId = "DeleteBook")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Book deleted",
            headers = [Header(
                name = "Location", description = "URI of the updated book",
                schema = Schema(type = "string", format = "uri")
            )],
            content = [Content(schema = Schema(hidden = true))]),
        ApiResponse(responseCode = "400", description = "Validation error",
            content = [Content(schema = Schema(implementation = MethodArgumentNotValidException::class))]),
        ApiResponse(responseCode = "404", description = "Book not found",
            content = [Content(schema = Schema(implementation = BookNotFoundException::class))])
    )
    @RequestMapping(
        value = ["/books/{isbn}"],
        method = [RequestMethod.DELETE]
    )
    fun deleteBook(@PathVariable isbn: String): ResponseEntity<Unit>

    //#####################################################

    @Operation(summary = "List all reviews", operationId = "listReviews")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "List of all reviews"),

        ApiResponse(responseCode = "404", description = "Review not found",
            content = [Content(schema = Schema(implementation = ReviewNotFoundException::class))])
            )

    @RequestMapping(
        value = ["/books/{isbn}/reviews"],
        produces = ["application/json"],
        method = [RequestMethod.GET]
    )
    fun listReviews(@PathVariable isbn: String): ResponseEntity<List<ReviewResponse>>


    //#####################################################

    @Operation(summary = "Create a new review", operationId = "createReview")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Review created",
            headers = [Header(
                name = "Location", description = "URI of the created book",
                schema = Schema(type = "string", format = "uri")
            )],
            content = [Content(schema = Schema(hidden = true))]),
        ApiResponse(responseCode = "400", description = "Validation error",
            content = [Content(schema = Schema(implementation = MethodArgumentNotValidException::class))]),
        ApiResponse(responseCode = "404", description = "There is no book registered to this isbn. Impossible to create review",
            content = [Content(schema = Schema(implementation = BookNotFoundException::class))])
    )
    @RequestMapping(
        value = ["/books/{isbn}/reviews"],
        consumes = ["application/json"],
        method = [RequestMethod.POST]
    )
    fun createReview(@PathVariable isbn: String, @Valid @RequestBody request: CreateReviewRequest): ResponseEntity<Unit>

    //#####################################################

    @Operation(summary = "Replace a review", operationId = "replaceReview")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Review replaced",
            headers = [Header(
                name = "Location", description = "URI of the replaced review",
                schema = Schema(type = "string", format = "uri")
            )],
            content = [Content(schema = Schema(hidden = true))]),

        ApiResponse(responseCode = "400", description = "Validation error",
            content = [Content(schema = Schema(implementation = MethodArgumentNotValidException::class))]),

        ApiResponse(responseCode = "404", description = "There is no such review/book",
            content = [Content(schema = Schema(implementation = ReviewNotFoundException::class))])
    )
    @RequestMapping(
        value = ["/books/{isbn}/reviews/{reviewId}"],
        consumes = ["application/json"],
        method = [RequestMethod.PUT]
    )
    fun replaceReview(@PathVariable reviewId: UUID, @Valid @RequestBody request: ReplaceReviewRequest): ResponseEntity<Unit>


    //#####################################################


    @Operation(summary = "Delete a review", operationId = "DeleteReview")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Review deleted",
            headers = [Header(
                name = "Location", description = "URI of the deleted review",
                schema = Schema(type = "string", format = "uri")
            )],
            content = [Content(schema = Schema(hidden = true))]),
        ApiResponse(responseCode = "400", description = "Validation error",
            content = [Content(schema = Schema(implementation = MethodArgumentNotValidException::class))]),
        ApiResponse(responseCode = "404", description = "Review not found",
            content = [Content(schema = Schema(implementation = ReviewNotFoundException::class))])
    )
    @RequestMapping(
        value = ["/books/{isbn}/reviews/{reviewId}"],
        method = [RequestMethod.DELETE]
    )
    fun deleteReview(@PathVariable isbn: String, @PathVariable reviewId: UUID): ResponseEntity<Unit>
}