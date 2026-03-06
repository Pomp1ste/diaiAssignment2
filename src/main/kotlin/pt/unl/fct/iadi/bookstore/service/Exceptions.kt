package pt.unl.fct.iadi.bookstore.service

import com.sun.tools.classfile.Attribute.Exceptions

object BookNotFoundException : RuntimeException("Book not found") {
    private fun readResolve(): Any = BookNotFoundException
}

object AlreadyExists : RuntimeException("Book already exists") {
    private fun readResolve(): Any = AlreadyExists
}