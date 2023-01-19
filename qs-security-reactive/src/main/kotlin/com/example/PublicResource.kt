package com.example

import javax.annotation.security.PermitAll
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/api/public")
class PublicResource {
  @GET
  @PermitAll
  @Produces(MediaType.TEXT_PLAIN)
  fun publicResource() = "public"
}