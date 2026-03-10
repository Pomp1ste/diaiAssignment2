package pt.unl.fct.iadi.bookstore.service


object BookNotFoundException : RuntimeException("Book not found") {
    private fun readResolve(): Any = BookNotFoundException
}

object AlreadyExists : RuntimeException("Book already exists") {
    private fun readResolve(): Any = AlreadyExists
}

object ReviewNotFoundException : RuntimeException("Review not found") {
    private fun readResolve(): Any = ReviewNotFoundException
}