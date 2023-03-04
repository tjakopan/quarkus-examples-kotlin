package com.example

import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.Validator
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/books")
class BookResource(private val validator: Validator, private val bookService: BookService) {
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  suspend fun hello(): String = "hello"

  @Path("/manual-validation")
  @POST
  suspend fun tryMeManualValidation(book: Book): Result {
    val violations = validator.validate(book)
    return if (violations.isEmpty()) Result("Book is valid! It was validated by manual validation.")
    else Result(violations)
  }

  @Path("/end-point-method-validation")
  @POST
  suspend fun tryMeEndPointMethodValidation(@Valid book: Book): Result =
    Result("Book is valid! It was validated by end point method validation.")

  @Path("/service-method-validation")
  @POST
  suspend fun tryMeServiceMethodValidation(book: Book): Result =
    try {
      bookService.validateBook(book).awaitSuspending()
      Result("Book is valid! It was validated by service method validation.")
    } catch (e: ConstraintViolationException) {
      Result(e.constraintViolations)
    }

  class Result(val message: String, val success: Boolean = true) {
    constructor(violations: Set<ConstraintViolation<*>>) : this(
      violations.joinToString(separator = ", ") { it.message },
      false
    )
  }
}
