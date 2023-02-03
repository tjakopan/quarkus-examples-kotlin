package com.example

import io.quarkus.test.junit.QuarkusTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.`is`
import kotlin.test.Test

@QuarkusTest
class IncrementResourceTest {
  @Test
  fun `test redis operations`() {
    Given {
      accept(ContentType.JSON)
    } When {
      get("/increments")
    } Then {
      statusCode(200)
      body("size()", `is`(0))
    }

    Given {
      contentType(ContentType.JSON)
      accept(ContentType.JSON)
      body("{\"key\":\"first-key\",\"value\":0}")
    } When {
      post("/increments")
    } Then {
      statusCode(200)
      body("key", `is`("first-key"))
      body("value", `is`(0))
    }

    Given {
      contentType(ContentType.JSON)
      accept(ContentType.JSON)
      body("{\"key\":\"second-key\",\"value\":10}")
    } When {
      post("/increments")
    } Then {
      statusCode(200)
      body("key", `is`("second-key"))
      body("value", `is`(10))
    }

    Given {
      contentType(ContentType.JSON)
      body("1")
    } When {
      put("/increments/first-key")
    } Then {
      statusCode(204)
    }

    Given {
      accept(ContentType.JSON)
    } When {
      get("/increments/first-key")
    } Then {
      statusCode(200)
      body("key", `is`("first-key"))
      body("value", `is`(1))
    }

    Given {
      contentType(ContentType.JSON)
      body("1000")
    } When {
      put("/increments/second-key")
    } Then {
      statusCode(204)
    }

    Given {
      accept(ContentType.JSON)
    } When {
      get("/increments/second-key")
    } Then {
      statusCode(200)
      body("key", `is`("second-key"))
      body("value", `is`(1010))
    }

    Given {
      accept(ContentType.JSON)
    } When {
      get("/increments")
    } Then {
      statusCode(200)
      body("size()", `is`(2))
    }

    Given {
      accept(ContentType.JSON)
    } When {
      delete("/increments/first-key")
    } Then {
      statusCode(204)
    }

    Given {
      accept(ContentType.JSON)
    } When {
      get("/increments")
    } Then {
      statusCode(200)
      body("size()", `is`(1))
    }

    Given {
      accept(ContentType.JSON)
    } When {
      delete("/increments/second-key")
    } Then {
      statusCode(204)
    }

    Given {
      accept(ContentType.JSON)
    } When {
      get("/increments")
    } Then {
      statusCode(200)
      body("size()", `is`(0))
    }
  }
}
