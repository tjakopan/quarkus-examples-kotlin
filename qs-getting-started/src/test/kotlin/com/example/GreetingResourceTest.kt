package com.example

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.`is`
import java.util.UUID
import kotlin.test.Test

@QuarkusTest
class GreetingResourceTest {
  @Test
  fun `test hello endpoint`() {
    When {
      get("/hello")
    } Then {
      statusCode(200)
      body(`is`("hello"))
    }
  }

  @Test
  fun `test greeting endpoint`() {
    val uuid = UUID.randomUUID().toString()
    Given {
      pathParam("name", uuid)
    } When {
      get("/hello/greeting/{name}")
    } Then {
      statusCode(200)
      body(`is`("hello $uuid"))
    }
  }
}