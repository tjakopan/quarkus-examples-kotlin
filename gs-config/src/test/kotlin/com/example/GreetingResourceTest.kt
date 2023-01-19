package com.example

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.`is`
import kotlin.test.Test

@QuarkusTest
class GreetingResourceTest {
  @Test
  fun `test hello endpoint`() {
    When {
      get("/greeting")
    } Then {
      statusCode(200)
      body(`is`("hello quarkus!"))
    }
  }
}