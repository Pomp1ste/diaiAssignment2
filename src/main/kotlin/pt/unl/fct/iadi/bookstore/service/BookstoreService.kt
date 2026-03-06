package pt.unl.fct.iadi.bookstore.service

import jakarta.validation.constraints.NotBlank
import org.springframework.stereotype.Component
import pt.unl.fct.iadi.bookstore.controller.dto.GetBookResponse
import pt.unl.fct.iadi.bookstore.domain.Book


@Component
class BookstoreService {
    private var books: MutableMap<String, Book> = mutableMapOf()

    fun listBooks(): List<GetBookResponse> = books.values.toList().map { GetBookResponse.fromBook(it) }

    fun getBook(@NotBlank isbn: String): Book {
        if (books.containsKey(isbn)) {
            return books[isbn]!!
        }
        else {
            throw BookNotFoundException
        }
    }

    fun createBook(book: Book): Book {
        if (books.containsKey(book.isbn)) {
            throw AlreadyExists
        }
        else {
            books[book.isbn] = book
            return book
        }
    }
}