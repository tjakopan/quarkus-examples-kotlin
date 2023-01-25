package com.example

import io.quarkus.cache.CacheResult
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.core.Vertx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import utilities.service.callService
import utilities.vertx.dispatcher
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import kotlin.coroutines.CoroutineContext

@ApplicationScoped
class WeatherForecastService(private val vertx: Vertx) : CoroutineScope, AutoCloseable {
  override val coroutineContext: CoroutineContext by lazy { vertx.dispatcher() + SupervisorJob() }

  @CacheResult(cacheName = "weather-cache")
  fun getDailyForecast(date: LocalDate, city: String): Uni<String> = callService {
    delay(2000L)
    "${date.dayOfWeek} will be ${getDailyResult(date.dayOfMonth % 4)} in $city"
  }

  private fun getDailyResult(dayOfMonthModuloFour: Int): String =
    when (dayOfMonthModuloFour) {
      0 -> "sunny"
      1 -> "cloudy"
      2 -> "chilly"
      3 -> "rainy"
      else -> throw IllegalArgumentException()
    }

  override fun close() {
    coroutineContext.cancel()
  }
}