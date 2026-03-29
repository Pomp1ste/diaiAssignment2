package pt.unl.fct.iadi.bookstore.service


class BookNotFoundException : RuntimeException("Book not found")

class AlreadyExists : RuntimeException("Book already exists")

class ReviewNotFoundException : RuntimeException("Review not found")