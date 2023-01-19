package com.example

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.`is`
import kotlin.test.Test

@QuarkusTest
class ReactiveSecurityRealmTest {
  @Test
  fun `should access public when anonymous`() {
    When {
      get("/api/public")
    } Then {
      statusCode(HttpStatus.SC_OK)
    }
  }

  @Test
  fun `should not access admin when anonymous`() {
    When {
      get("/api/admin")
    } Then {
      statusCode(HttpStatus.SC_UNAUTHORIZED)
    }
  }

  @Test
  fun `should access admin when admin authenticated`() {
    Given {
      auth().preemptive().basic("admin", "admin")
      log().all()
    } When {
      get("/api/admin")
    } Then {
      statusCode(HttpStatus.SC_OK)
    }
  }

  @Test
  fun `should not access user when admin authenticated`() {
    Given {
      auth().preemptive().basic("admin", "admin")
    } When {
      get("/api/users/me")
    } Then {
      statusCode(HttpStatus.SC_FORBIDDEN)
    }
  }

  @Test
  fun `should access user and get identity when user authenticated`() {
    Given {
      auth().preemptive().basic("user", "user")
    } When {
      get("/api/users/me")
    } Then {
      statusCode(HttpStatus.SC_OK)
      body(`is`("user"))
    }
  }
}