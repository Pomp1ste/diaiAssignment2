package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import pt.unl.fct.iadi.bookstore.domain.Book

data class ReplaceBookRequest (
    @field:_root_ide_package_.jakarta.validation.constraints.NotBlank
    val isbn: String,
    @field:NotBlank
    val title: String,
    @field:NotBlank
    val author: String,
    @field:NotBlank
    val price: Double,
    @field:NotBlank
    val image: String
) {

}