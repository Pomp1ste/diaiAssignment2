package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import pt.unl.fct.iadi.bookstore.domain.Book

data class GetBookRequest (
    @field:_root_ide_package_.jakarta.validation.constraints.NotBlank
    val isbn: String
)