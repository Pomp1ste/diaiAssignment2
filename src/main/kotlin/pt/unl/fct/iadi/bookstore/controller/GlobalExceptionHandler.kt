package pt.unl.fct.iadi.bookstore.controller


import org.springframework.context.MessageSource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import pt.unl.fct.iadi.bookstore.service.ErrorResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import pt.unl.fct.iadi.bookstore.service.AlreadyExists
import pt.unl.fct.iadi.bookstore.service.BookNotFoundException
import pt.unl.fct.iadi.bookstore.service.ReviewNotFoundException

import java.util.Locale

@RestControllerAdvice
class GlobalExceptionHandler(private val messageSource: MessageSource) {

    private fun languageHeaders(locale: Locale): HttpHeaders {
        val headers = HttpHeaders()
        headers.set(HttpHeaders.CONTENT_LANGUAGE, locale.language)
        return headers
    }

    //if @valid fails
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val message = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.badRequest().body(ErrorResponse("VALIDATION_ERR", "parameters not consistent with the requirements"))
    }

    @ExceptionHandler(BookNotFoundException::class)
    fun handleBookNotFound(ex: BookNotFoundException, locale: Locale): ResponseEntity<ErrorResponse> {
        val message = messageSource.getMessage("error.book.notfound", null, "Book not found", locale)
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .headers(languageHeaders(locale))
            .body(ErrorResponse("NOT_FOUND", message!!))
    }

    @ExceptionHandler(ReviewNotFoundException::class)
    fun handleReviewNotFound(ex: ReviewNotFoundException, locale: Locale): ResponseEntity<ErrorResponse> {
        val message = messageSource.getMessage("error.review.notfound", null, "Review not found", locale)
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .headers(languageHeaders(locale))
            .body(ErrorResponse("NOT_FOUND", message!!))
    }

    @ExceptionHandler(AlreadyExists::class)
    fun handleAlreadyExists(ex: AlreadyExists, locale: Locale): ResponseEntity<ErrorResponse> {
        val message = messageSource.getMessage("error.alreadyExists", null, "Book already exists", locale)
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .headers(languageHeaders(locale))
            .body(ErrorResponse("CONFLICT", message!!))
    }
}