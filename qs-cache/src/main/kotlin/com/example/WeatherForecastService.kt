package com.example

import io.quarkus.cache.CacheResult
import io.smallrye.mutiny.Uni
import kotlinx.coroutines.delay
import utilities.service.AbstractService
import utilities.service.callService
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class WeatherForecastService : AbstractService() {
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
}