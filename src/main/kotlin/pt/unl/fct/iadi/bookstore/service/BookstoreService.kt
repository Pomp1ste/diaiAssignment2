package pt.unl.fct.iadi.bookstore.service

import jakarta.validation.constraints.NotBlank
import org.springframework.stereotype.Component
import pt.unl.fct.iadi.bookstore.controller.dto.GetBookResponse
import pt.unl.fct.iadi.bookstore.controller.dto.PartialUpdateRequest
import pt.unl.fct.iadi.bookstore.controller.dto.ReviewResponse
import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.domain.Review
import java.util.UUID


@Component
class BookstoreService {
    private var books: MutableMap<String, Book> = mutableMapOf()
    private var reviews: MutableMap<String, MutableList<Review>> = mutableMapOf()
    private var idToIsbn: MutableMap<String, String> = mutableMapOf()

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

    fun createReview(isbn: String, review: Review) {
        if (!books.containsKey(isbn)) throw BookNotFoundException
        reviews.getOrPut(isbn) { mutableListOf() }.add(review)
        idToIsbn[review.id.toString()] = isbn
    }

    fun listReviews(isbn: String): List<ReviewResponse> =
        reviews[isbn]?.map { ReviewResponse.fromReview(it) } ?: throw ReviewNotFoundException

    fun replaceReview(isbn: String, @NotBlank review: Review) {
        val isbn = idToIsbn[review.id.toString()]
        val bookReviews: MutableList<Review> = reviews[isbn] ?: throw BookNotFoundException
        for (i in bookReviews.indices) {
            if (bookReviews[i].id == review.id) {
                bookReviews[i] = review
                return
            }
        }
        throw ReviewNotFoundException
    }

    fun updateReview(isbn: String, reviewId: UUID, review: Review) {
        val listrev: MutableList<Review> = reviews[isbn] ?: throw BookNotFoundException
        for (i in listrev.indices) {
            if (listrev[i].id == reviewId) {
                var rev = listrev[i]
                rev = Review(
                    id = rev.id,
                    rating = rev.rating,
                    comment = rev.comment
                )
                return
            }
        }
        throw ReviewNotFoundException
    }

    fun deleteReview(@NotBlank revId: UUID) {
        val isbn = idToIsbn[revId.toString()]
        val bookReviews: MutableList<Review> = reviews[isbn] ?: throw BookNotFoundException
        for (i in bookReviews.indices) {
            if (bookReviews[i].id == revId) {
                bookReviews.remove(bookReviews[i])
                return
            }
        }
        throw ReviewNotFoundException
    }
}