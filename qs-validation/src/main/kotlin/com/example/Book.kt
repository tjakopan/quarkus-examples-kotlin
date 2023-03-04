package com.example

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

// This class has to look exactly like that, otherwise tests for manual validation fail.
// If we're not going to do any manual validation then fields can be final, declared inside constructor, but even then,
// in case of invalid book, the rest call will just fail with 400, without any validation error details.
class Book {
  @NotBlank(message = "Title cannot be blank")
  lateinit var title: String

  @NotBlank(message = "Author cannot be blank")
  lateinit var author: String

  @Min(message = "Author has been very lazy", value = 1)
  var pages: Double = 0.0
}
