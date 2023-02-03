package com.example

import javax.ws.rs.*

@Path("/increments")
class IncrementResource(private val service: IncrementService) {
  @GET
  suspend fun keys(): List<String> = service.keys()

  @POST
  suspend fun create(increment: Increment): Increment {
    service.set(increment.key, increment.value)
    return increment
  }

  @GET
  @Path("/{key}")
  suspend fun get(key: String): Increment = Increment(key, service.get(key))

  @PUT
  @Path("/{key}")
  suspend fun increment(key: String, value: Long) = service.increment(key, value)

  @DELETE
  @Path("/{key}")
  suspend fun delete(key: String) = service.del(key)
}
