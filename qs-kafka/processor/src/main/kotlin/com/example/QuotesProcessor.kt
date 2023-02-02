package com.example

import io.smallrye.mutiny.Uni
import kotlinx.coroutines.delay
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing
import utilities.service.AbstractService
import utilities.service.callService
import javax.enterprise.context.ApplicationScoped
import kotlin.random.Random

@ApplicationScoped
class QuotesProcessor : AbstractService() {
  @Incoming("quote-requests")
  @Outgoing("quotes")
  fun process(quoteRequest: String): Uni<Quote> = callService {
    delay(200)
    Quote(quoteRequest, Random.Default.nextInt(100))
  }
}