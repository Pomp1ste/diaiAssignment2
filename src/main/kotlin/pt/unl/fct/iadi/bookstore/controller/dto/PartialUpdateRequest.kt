package pt.unl.fct.iadi.bookstore.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class PartialUpdateRequest(
    @field:Size(min = 1, max = 80)
    val author: String?,
    @field:Size(min = 1, max = 120)
    val title: String?,
    @field:Positive
    val price: Double?,
    @field:Pattern(regexp = "^https://.*", message = "image must start with https://")
    val image: String?,

)