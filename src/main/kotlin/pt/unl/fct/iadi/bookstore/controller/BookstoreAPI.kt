package pt.unl.fct.iadi.bookstore.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import pt.unl.fct.iadi.bookstore.controller.dto.CreateBookRequest
import pt.unl.fct.iadi.bookstore.controller.dto.GetBookResponse
import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.service.AlreadyExists
import pt.unl.fct.iadi.bookstore.service.BookNotFoundException


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

    @Operation(summary = "Get a single book", operationId = "singleBook")
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
    fun putBook(@Valid @RequestBody request: CreateBookRequest): ResponseEntity<Unit>

}