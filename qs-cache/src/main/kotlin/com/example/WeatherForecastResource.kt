package com.example

import io.smallrye.mutiny.coroutines.awaitSuspending
import org.jboss.resteasy.reactive.RestQuery
import java.time.LocalDate
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/weather")
class WeatherForecastResource(private val service: WeatherForecastService) {
  @GET
  suspend fun getForecast(@RestQuery city: String, @RestQuery daysInFuture: Long): WeatherForecast {
    val executionStart = System.currentTimeMillis()
    val dailyForecasts = listOf(
      service.getDailyForecast(LocalDate.now().plusDays(daysInFuture), city).awaitSuspending(),
      service.getDailyForecast(LocalDate.now().plusDays(daysInFuture + 1), city).awaitSuspending(),
      service.getDailyForecast(LocalDate.now().plusDays(daysInFuture + 2), city).awaitSuspending()
    )
    val executionEnd = System.currentTimeMillis()
    return WeatherForecast(dailyForecasts, executionEnd - executionStart)
  }
}