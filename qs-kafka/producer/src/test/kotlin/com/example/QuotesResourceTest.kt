package com.example

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import java.util.*
import kotlin.test.Test

@QuarkusTest
class QuotesResourceTest {
    @Test
    fun `test quotes event stream`() {
        val body = When {
            post("/quotes/request")
        } Then {
            statusCode(200)
        } Extract {
            body().asString()
        }

        shouldNotThrowAny { UUID.fromString(body) }
    }
}