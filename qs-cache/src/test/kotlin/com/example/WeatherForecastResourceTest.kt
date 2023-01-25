package com.example

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.containsString
import kotlin.test.Test

@QuarkusTest
class WeatherForecastResourceTest {
  @Test
  fun `test weather endpoint`() {
    Given {
      queryParam("city", "Zagreb")
    } When {
      get("/weather")
    } Then {
      statusCode(200)
      body(containsString("dailyForecasts"))
    }
  }
}