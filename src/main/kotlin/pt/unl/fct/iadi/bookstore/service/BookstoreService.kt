package pt.unl.fct.iadi.bookstore.service

import jakarta.validation.constraints.NotBlank
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.bookstore.controller.dto.CreateBookRequest
import pt.unl.fct.iadi.bookstore.controller.dto.GetBookResponse
import pt.unl.fct.iadi.bookstore.controller.dto.PartialUpdateRequest
import pt.unl.fct.iadi.bookstore.controller.dto.ReviewResponse
import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.domain.Review
import java.util.UUID


@Service
class BookstoreService {
    private var books: MutableMap<String, Book> = mutableMapOf()
    private var reviews: MutableMap<String, MutableList<Review>> = mutableMapOf()
    private var idToIsbn: MutableMap<String, String> = mutableMapOf()


    fun isAuthor(isbn: String, reviewId: UUID, authorName: String) : Boolean {
        val reviews = reviews[isbn] ?: return false
        for (review in reviews) {
            if (review.id == reviewId) {
                if (authorName == review.author) return true
            }
        }
        return false
    }

    fun listBooks(): List<GetBookResponse> = books.values.toList().map { GetBookResponse.fromBook(it) }

    fun getBook(@NotBlank isbn: String): Book {
        return books[isbn] ?: throw BookNotFoundException()
    }

    fun createBook(book: Book): Book {
        if (books.containsKey(book.isbn)) {
            throw AlreadyExists()
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

    fun updateBook(isbn: String, updateRequest: PartialUpdateRequest): Book {
        val existing = books[isbn] ?: throw BookNotFoundException()

        val new: Book = CreateBookRequest(
            isbn = isbn,
            title = updateRequest.title ?: existing.title,
            price = updateRequest.price ?: existing.price,
            image = updateRequest.image ?: existing.image,
            author = updateRequest.author ?: existing.author
        ).toBook()

        books[isbn] = new
        return new
    }

    fun deleteBook(isbn: String) {
        books.remove(isbn) ?: throw BookNotFoundException()
        reviews.remove(isbn)
    }

    fun createReview(isbn: String, review: Review) {
        if (!books.containsKey(isbn)) throw BookNotFoundException()
        reviews.getOrPut(isbn) { mutableListOf() }.add(review)
        idToIsbn[review.id.toString()] = isbn
    }

    fun listReviews(isbn: String): List<ReviewResponse> =
        if (books[isbn] == null) {throw BookNotFoundException()}
        else {
            reviews[isbn]?.map { ReviewResponse.fromReview(it) } ?: emptyList()
        }

    fun replaceReview(isbn: String, @NotBlank review: Review) {
        val isbn = idToIsbn[review.id.toString()]
        val bookReviews: MutableList<Review> = reviews[isbn] ?: throw BookNotFoundException()
        for (i in bookReviews.indices) {
            if (bookReviews[i].id == review.id) {
                review.author = bookReviews[i].author
                bookReviews[i] = review
                return
            }
        }
        throw ReviewNotFoundException()
    }

    fun updateReview(isbn: String, reviewId: UUID, review: Review) {
        val listrev: MutableList<Review> = reviews[isbn] ?: throw BookNotFoundException()
        for (i in listrev.indices) {
            if (listrev[i].id == reviewId) {
                listrev[i] = Review(
                    id = listrev[i].id,
                    rating = review.rating ?: listrev[i].rating,
                    comment = listrev[i].comment,
                    author = listrev[i].author
                )
                return
            }
        }
        throw ReviewNotFoundException()
    }

    fun deleteReview(@NotBlank revId: UUID) {
        val isbn = idToIsbn[revId.toString()]
        val bookReviews: MutableList<Review> = reviews[isbn] ?: throw BookNotFoundException()
        for (i in bookReviews.indices) {
            if (bookReviews[i].id == revId) {
                bookReviews.remove(bookReviews[i])
                return
            }
        }
        throw ReviewNotFoundException()
    }
}