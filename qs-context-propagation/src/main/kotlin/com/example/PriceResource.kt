package com.example

import io.smallrye.mutiny.Uni
import io.smallrye.reactive.messaging.MutinyEmitter
import org.eclipse.microprofile.reactive.messaging.Channel
import javax.ws.rs.POST
import javax.ws.rs.Path

@Path("/")
class PriceResource(@Channel("prices") private val emitter: MutinyEmitter<Double>) {
  @POST
  fun postAPrice(price: Price): Uni<Void> =
    if (emitter.hasRequests()) emitter.send(price.value)
    else Uni.createFrom().nullItem()
}
