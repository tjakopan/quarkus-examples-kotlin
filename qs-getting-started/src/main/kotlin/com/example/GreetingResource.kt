package com.example

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/hello")
class GreetingResource(private val service: GreetingService) {
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("/greeting/{name}")
  fun greeting(name: String) = service.greeting(name)

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  fun hello() = "hello"
}