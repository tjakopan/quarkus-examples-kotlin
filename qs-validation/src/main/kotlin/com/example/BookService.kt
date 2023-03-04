package com.example

import io.smallrye.mutiny.Uni
import utilities.service.AbstractService
import javax.enterprise.context.ApplicationScoped
import javax.validation.Valid

@ApplicationScoped
class BookService : AbstractService() {
  fun validateBook(@Valid book: Book): Uni<Void> {
    // your business logic here
    return Uni.createFrom().item { null }
  }
}
