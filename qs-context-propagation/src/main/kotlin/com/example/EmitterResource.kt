package com.example

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.infrastructure.Infrastructure
import org.eclipse.microprofile.reactive.messaging.Channel
import org.jboss.resteasy.reactive.RestStreamElementType
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/")
class EmitterResource(@Channel("prices") private val prices: Multi<Double>) {
  @GET
  @Path("/prices")
  @Produces(MediaType.SERVER_SENT_EVENTS)
  @RestStreamElementType(MediaType.TEXT_PLAIN)
  @Transactional
  fun prices(): Multi<Double> =
    prices.select().first(3)
      // Items are received from the event loop, so cannot use Hibernate ORM (classic).
      // Switch to a worker thread, the transaction will be propagated.
      .emitOn(Infrastructure.getDefaultExecutor())
      .map { price ->
        Price().apply {
          value = price
          persist()
        }
        price
      }

  @GET
  @Path("/prices/all")
  fun getAllPrices(): List<Price> = Price.listAll()
}
