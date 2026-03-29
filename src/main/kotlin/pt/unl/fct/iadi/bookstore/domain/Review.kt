package pt.unl.fct.iadi.bookstore.domain

import java.util.UUID

data class Review (
    val id: UUID,
    val rating: Int? = null,
    val comment: String? = null,
    var author: String? = null
){
}