package com.example

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.vertx.core.json.JsonObject
import org.hamcrest.CoreMatchers.*
import kotlin.test.Test

@QuarkusTest
class BookResourceTest {
  @Test
  fun `test hello endpoint`() {
    When {
      get("/books")
    } Then {
      statusCode(200)
      body(`is`("hello"))
    }
  }

  @Test
  fun `test valid book`() {
    Given {
      body(JsonObject(mapOf("title" to "some book", "author" to "me", "pages" to 5)).encode())
      contentType("application/json")
    } When {
      post("/books/manual-validation")
    } Then {
      statusCode(200)
      body("success", `is`(true), "message", containsString("Book is valid!"))
    }
  }

  @Test
  fun `test book without title`() {
    Given {
      body(JsonObject(mapOf("author" to "me", "pages" to 5)).encode())
      contentType("application/json")
    } When {
      post("/books/manual-validation")
    } Then {
      statusCode(200)
      body("success", `is`(false), "message", containsString("Title"))
    }
  }

  @Test
  fun `test book without author`() {
    Given {
      body(JsonObject(mapOf("title" to "catchy", "pages" to 5)).encode())
      contentType("application/json")
    } When {
      post("/books/manual-validation")
    } Then {
      statusCode(200)
      body("success", `is`(false), "message", containsString("Author"))
    }
  }

  @Test
  fun `test book with negative page`() {
    Given {
      body(JsonObject(mapOf("title" to "some book", "author" to "me", "pages" to -25)).encode())
      contentType("application/json")
    } When {
      post("/books/manual-validation")
    } Then {
      statusCode(200)
      body("success", `is`(false), "message", containsString("lazy"))
    }
  }

  @Test
  fun `test valid book - end-point validation`() {
    Given {
      body(JsonObject(mapOf("title" to "some book", "author" to "me", "pages" to 5)).encode())
      contentType("application/json")
    } When {
      post("/books/end-point-method-validation")
    } Then {
      statusCode(200)
      body("success", `is`(true), "message", containsString("Book is valid!"))
    }
  }

  @Test
  fun `test book without title - end-point validation`() {
    Given {
      body(JsonObject(mapOf("author" to "me", "pages" to 5)).encode())
      contentType("application/json")
    } When {
      post("/books/end-point-method-validation")
    } Then {
      statusCode(400)
      body("violations.message", hasItem("Title cannot be blank"))
    }
  }

  @Test
  fun `test valid book - service validation`() {
    Given {
      body(JsonObject(mapOf("title" to "some book", "author" to "me", "pages" to 5)).encode())
      contentType("application/json")
    } When {
      post("/books/service-method-validation")
    } Then {
      statusCode(200)
      body("success", `is`(true), "message", containsString("Book is valid!"))
    }
  }

  @Test
  fun `test book without title - service validation`() {
    Given {
      body(JsonObject(mapOf("author" to "me", "pages" to 5)).encode())
      contentType("application/json")
    } When {
      post("/books/service-method-validation")
    } Then {
      statusCode(200)
      body("success", `is`(false), "message", containsString("Title"))
    }
  }
}
