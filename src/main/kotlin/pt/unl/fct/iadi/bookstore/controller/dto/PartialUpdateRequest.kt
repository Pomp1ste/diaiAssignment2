package pt.unl.fct.iadi.bookstore.controller.dto

data class PartialUpdateRequest(
    @field:_root_ide_package_.jakarta.validation.constraints.NotBlank
    val isbn: String,
    val author: String?,
    val title: String?,
    val price: Double?,
    val image: String?,

)