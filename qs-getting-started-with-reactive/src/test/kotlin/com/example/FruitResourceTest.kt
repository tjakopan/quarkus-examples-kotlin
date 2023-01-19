package com.example

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.text.IsEmptyString.emptyString
import kotlin.test.Test

@QuarkusTest
class FruitResourceTest {
  @Test
  fun `test getting all fruits`() {
    var response = When {
      get("/fruits")
    } Then {
      statusCode(200)
      contentType("application/json")
    } Extract {
      response()
    }
    response.jsonPath().getList<String>("name") shouldContainExactlyInAnyOrder listOf("Cherry", "Apple", "Banana")

    Given {
      body("{\"name\": \"Pineapple\"}")
      contentType("application/json")
    } When {
      put("/fruits/1")
    } Then {
      statusCode(200)
      body(containsString("\"id\":"), containsString("\"name\":\"Pineapple\""))
    }

    response = When {
      get("/fruits")
    } Then {
      statusCode(200)
      contentType("application/json")
    } Extract {
      response()
    }
    response.jsonPath().getList<String>("name") shouldContainExactlyInAnyOrder listOf("Pineapple", "Apple", "Banana")

    When {
      delete("/fruits/1")
    } Then {
      statusCode(204)
    }

    response = When {
      get("/fruits")
    } Then {
      statusCode(200)
      contentType("application/json")
    } Extract {
      response()
    }
    response.jsonPath().getList<String>("name") shouldContainExactlyInAnyOrder listOf("Apple", "Banana")

    Given {
      body("{\"name\": \"Pear\"}")
      contentType("application/json")
    } When {
      post("/fruits")
    } Then {
      statusCode(201)
      body(containsString("\"id\":"), containsString("\"name\":\"Pear\""))
    }

    response = When {
      get("/fruits")
    } Then {
      statusCode(200)
      contentType("application/json")
    } Extract {
      response()
    }
    response.jsonPath().getList<String>("name") shouldContainExactlyInAnyOrder listOf("Pear", "Apple", "Banana")
  }

  @Test
  fun `test entity not found for delete`() {
    When {
      delete("/fruits/9236")
    } Then {
      statusCode(404)
      body(emptyString())
    }
  }

  @Test
  fun `test entity not found for update`() {
    Given {
      body("{\"name\": \"Watermelon\"}")
      contentType("application/json")
    } When {
      put("/fruits/32432")
    } Then {
      statusCode(404)
      body(emptyString())
    }
  }
}