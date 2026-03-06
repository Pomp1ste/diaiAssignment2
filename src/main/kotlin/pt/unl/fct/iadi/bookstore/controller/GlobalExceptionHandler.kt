package pt.unl.fct.iadi.bookstore.controller


import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import pt.unl.fct.iadi.bookstore.service.ErrorResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import pt.unl.fct.iadi.bookstore.service.BookNotFoundException

import java.util.Locale

@RestControllerAdvice
class GlobalExceptionHandler(private val messageSource: MessageSource) {

    //if @valid fails
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val message = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.badRequest().body(ErrorResponse("VALIDATION_ERR", "parameters not consistent with the requirements"))
    }

    @ExceptionHandler(BookNotFoundException::class)
    fun handleBookNotFound(ex: BookNotFoundException, locale: Locale): ResponseEntity<String> {
        val message = messageSource.getMessage("error.book.notfound", null, locale)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message)
    }


}