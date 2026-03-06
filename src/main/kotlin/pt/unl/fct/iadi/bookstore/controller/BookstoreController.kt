package pt.unl.fct.iadi.bookstore.controller

import pt.unl.fct.iadi.bookstore.controller.dto.CreateBookRequest
import pt.unl.fct.iadi.bookstore.service.BookstoreService
import org.springframework.web.bind.annotation.RestController

@RestController
class BookstoreController(private val service: BookstoreService): BookstoreAPI {
    override fun createBook(request: CreateBookRequest): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }

}