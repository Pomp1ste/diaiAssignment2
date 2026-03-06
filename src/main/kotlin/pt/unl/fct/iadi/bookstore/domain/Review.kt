package pt.unl.fct.iadi.bookstore.domain

import java.util.UUID

data class Review (
    val id: UUID,
    val rating: Int,
    val comment: String
){
}