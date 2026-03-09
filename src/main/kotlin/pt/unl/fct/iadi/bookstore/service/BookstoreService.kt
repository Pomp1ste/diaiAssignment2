package pt.unl.fct.iadi.bookstore.service

import jakarta.validation.constraints.NotBlank
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import pt.unl.fct.iadi.bookstore.controller.dto.CreateReviewRequest
import pt.unl.fct.iadi.bookstore.controller.dto.GetBookResponse
import pt.unl.fct.iadi.bookstore.controller.dto.PartialUpdateRequest
import pt.unl.fct.iadi.bookstore.controller.dto.ReviewResponse
import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.domain.Review


@Component
class BookstoreService {
    private var books: MutableMap<String, Book> = mutableMapOf()
    private var reviews: MutableMap<String, MutableList<Review>> = mutableMapOf()

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

    fun putBook(isbn: String, @NotBlank book: Book): Pair<Book, Boolean> {
        val created = !books.containsKey(book.isbn)
        books[book.isbn] = book
        return Pair(book, created)
    }

    fun updateBook(isbn: String, updateRequest: PartialUpdateRequest) {
        val existing = books[isbn] ?: throw BookNotFoundException
        books[isbn] = existing.copy(
            author = updateRequest.author ?: existing.author,
            title = updateRequest.title ?: existing.title,
            price = updateRequest.price ?: existing.price,
            image = updateRequest.image ?: existing.image
        )
    }

    fun deleteBook(@NotBlank isbn: String) {
        val result = books.remove(isbn) ?: throw BookNotFoundException
        reviews.remove(result.isbn)
    }

    fun createReview(review: CreateReviewRequest) {
        if (!books.containsKey(review.isbn)) throw BookNotFoundException
        reviews.getOrPut(review.isbn) { mutableListOf() }.add(review.toReview())
    }

    fun listReviews(isbn: String): List<ReviewResponse> =
        reviews[isbn]?.map { ReviewResponse.fromReview(it) } ?: throw ReviewNotFoundException

}