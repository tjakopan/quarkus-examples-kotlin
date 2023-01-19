package com.example

import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/api/admin")
class AdminResource {
  @GET
  @RolesAllowed("admin")
  @Produces(MediaType.TEXT_PLAIN)
  fun adminResource() = "admin"
}