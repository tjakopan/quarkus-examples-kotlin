package com.example

import io.smallrye.mutiny.coroutines.awaitSuspending
import io.smallrye.reactive.messaging.MutinyEmitter
import org.eclipse.microprofile.reactive.messaging.Channel
import utilities.mutiny.replaceWithUnit
import javax.ws.rs.POST
import javax.ws.rs.Path

@Path("/")
class PriceResource(@Channel("prices") private val emitter: MutinyEmitter<Double>) {
  @POST
  suspend fun postAPrice(price: Price) {
    if (emitter.hasRequests()) return emitter.send(price.value).replaceWithUnit().awaitSuspending()
  }
}