package com.example

import io.quarkus.cache.Cache
import io.quarkus.cache.CacheName
import io.quarkus.cache.CacheResult
import io.quarkus.cache.CompositeCacheKey
import kotlinx.coroutines.delay
import org.jboss.resteasy.reactive.server.runtime.kotlin.ApplicationCoroutineScope
import org.jboss.resteasy.reactive.server.runtime.kotlin.VertxDispatcher
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import kotlin.time.Duration.Companion.milliseconds

@ApplicationScoped
class WeatherForecastService(
  @CacheName("weather-cache") private val weatherCache: Cache,
  private val coroutineScope: ApplicationCoroutineScope,
  private val coroutineDispatcher: VertxDispatcher
) {
  @CacheResult(cacheName = "weather-cache")
  suspend fun getDailyForecast(date: LocalDate, city: String): String {
    val key = CompositeCacheKey(date, city)
    return coroutineScope.
    return weatherCache.get(key) { k ->

    }
    delay(2000.milliseconds)
    return "${date.dayOfWeek} will be ${getDailyResult(date.dayOfMonth % 4)} in $city"
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