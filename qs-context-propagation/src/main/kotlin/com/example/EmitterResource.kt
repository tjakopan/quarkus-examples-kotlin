package com.example

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.eclipse.microprofile.reactive.messaging.Channel
import org.jboss.resteasy.reactive.RestStreamElementType
import utilities.panache.withTransaction
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
  suspend fun prices(): Multi<Double> = withTransaction {
    Multi.createFrom().publisher(prices)
      .select().first(3)
//      .emitOn(Infrastructure.getDefaultExecutor())
      .map { price ->
        Price().apply {
          value = price
          persist<Price>()
        }
        price
      }
  }

  @GET
  @Path("/prices/all")
  suspend fun getAllPrices(): List<Price> = Price.listAll().awaitSuspending()
}