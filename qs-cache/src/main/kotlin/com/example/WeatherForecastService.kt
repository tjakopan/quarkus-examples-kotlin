package com.example

import io.quarkus.cache.Cache
import io.quarkus.cache.CacheName
import io.quarkus.cache.CompositeCacheKey
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.mutiny.core.Vertx
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class WeatherForecastService(@CacheName("weather-cache") private val weatherCache: Cache, private val vertx: Vertx) {
  /*
   * This is working, but still blocking main thread.
   * WARN  [io.ver.cor.imp.BlockedThreadChecker] (vertx-blocked-thread-checker) Thread Thread[#125,vert.x-eventloop-thread-0,5,main] has been blocked for 3984 ms, time limit is 2000 ms: io.vertx.core.VertxException: Thread
  suspend fun getDailyForecast(date: LocalDate, city: String): String {
    val key = CompositeCacheKey(date, city)
    return weatherCache.get(key) { loadValue(date, city) }.awaitSuspending()
  }

  @Blocking
  private fun loadValue(date: LocalDate, city: String): String {
    try {
      Thread.sleep(2000L)
    } catch (e: InterruptedException) {
      Thread.currentThread().interrupt()
    }
    return "${date.dayOfWeek} will be ${getDailyResult(date.dayOfMonth % 4)} in $city"
  }
   */

  /*
   * Not working: ERROR [org.jbo.res.rea.com.cor.AbstractResteasyReactiveContext] (vert.x-eventloop-thread-1) Request failed: java.lang.IllegalStateException: The current thread cannot be blocked: vert.x-eventloop-thread-1
  suspend fun getDailyForecast(date: LocalDate, city: String): String {
    val key = CompositeCacheKey(date, city)
    return weatherCache.get(key) {
      vertx.executeBlockingAndAwait(Uni.createFrom().item {
        try {
          Thread.sleep(2000L)
        } catch (e: InterruptedException) {
          Thread.currentThread().interrupt()
        }
        "${date.dayOfWeek} will be ${getDailyResult(date.dayOfMonth % 4)} in $city"
      })
    }.awaitSuspending()
  }
   */

  /*
   * Working without warnings, but it always takes 6 seconds, because value loader function returns Uni, and it can't
   * be cached.
  suspend fun getDailyForecast(date: LocalDate, city: String): Uni<String> {
    val key = CompositeCacheKey(date, city)
    return weatherCache.get(key) {
      vertx.executeBlocking(Uni.createFrom().item {
        try {
          Thread.sleep(2000L)
        } catch (e: InterruptedException) {
          Thread.currentThread().interrupt()
        }
        "${date.dayOfWeek} will be ${getDailyResult(date.dayOfMonth % 4)} in $city"
      })
    }.awaitSuspending()
  }
   */

  fun getDailyForecast(date: LocalDate, city: String): Uni<String> {
    val key = CompositeCacheKey(date, city)
    return weatherCache.get(key) {
      try {
        Thread.sleep(2000L)
      } catch (e: InterruptedException) {
        Thread.currentThread().interrupt()
      }
      "${date.dayOfWeek} will be ${getDailyResult(date.dayOfMonth % 4)} in $city"
    }
  }

  private fun getDailyResult(dayOfMonthModuloFour: Int): String =
    when (dayOfMonthModuloFour) {
      0 -> "sunny"
      1 -> "cloudy"
      2 -> "chilly"
      3 -> "rainy"
      else -> throw IllegalArgumentException()
    }
}