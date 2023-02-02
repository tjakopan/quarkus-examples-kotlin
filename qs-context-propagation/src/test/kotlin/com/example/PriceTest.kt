package com.example

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.quarkus.test.common.http.TestHTTPResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.awaitility.Awaitility.await
import java.util.concurrent.CopyOnWriteArrayList
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.sse.SseEventSource
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

@QuarkusTest
class PriceTest {
  @TestHTTPResource("/prices")
  private lateinit var pricesSseEndpoint: String

  @Test
  fun test() {
    var prices = RestAssured.get("/prices/all").`as`(object : TypeRef<List<Price>>() {})
    prices.shouldBeEmpty()

    val webClient = ClientBuilder.newClient()
    val webTarget = webClient.target(pricesSseEndpoint)
    val receivedPrices = CopyOnWriteArrayList<Double>()
    val sseEventSource = SseEventSource.target(webTarget).build().apply {
      register { inboundSseEvent -> receivedPrices.add(inboundSseEvent.readData().toDouble()) }
      open()
    }

    val price1 = Price().apply { value = 1.0 }
    val price2 = Price().apply { value = 4.0 }
    val price3 = Price().apply { value = 2.0 }
    Given {
      header("Content-Type", "application/json")
      body(price1)
    } When {
      post("/")
    } Then {
      statusCode(204)
    }
    Given {
      header("Content-Type", "application/json")
      body(price2)
    } When {
      post("/")
    } Then {
      statusCode(204)
    }
    Given {
      header("Content-Type", "application/json")
      body(price3)
    } When {
      post("/")
    } Then {
      statusCode(204)
    }

    await().atMost(100000.milliseconds.toJavaDuration()).until { receivedPrices.size == 3 }
    sseEventSource.close()

    receivedPrices shouldContainAll listOf(price1.value, price2.value, price3.value)

    prices = RestAssured.get("/prices/all").`as`(object : TypeRef<List<Price>>() {})
    prices shouldHaveSize 3
  }
}
