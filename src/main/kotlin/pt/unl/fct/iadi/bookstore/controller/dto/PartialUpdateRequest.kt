package pt.unl.fct.iadi.bookstore.controller.dto

import jakarta.validation.constraints.NotBlank

data class PartialUpdateRequest(
    val author: String?,
    val title: String?,
    val price: Double?,
    val image: String?,

)