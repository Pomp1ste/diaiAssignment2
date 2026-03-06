package pt.unl.fct.iadi.bookstore.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import pt.unl.fct.iadi.bookstore.controller.dto.CreateBookRequest
import pt.unl.fct.iadi.bookstore.controller.dto.GetBookResponse
import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.service.BookstoreService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

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
            .header("X-Request-Id", requestId ?: "generated-id-123")
            .build()
    }

    override fun listBooks(): ResponseEntity<List<GetBookResponse>> {
        return ResponseEntity.ok(service.listBooks())
    }
}