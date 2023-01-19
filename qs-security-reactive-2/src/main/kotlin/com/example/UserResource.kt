package com.example

import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.SecurityContext

@Path("/api/users")
class UserResource {
  @GET
  @RolesAllowed("user")
  @Path("/me")
  fun me(@Context securityContext: SecurityContext): String = securityContext.userPrincipal.name
}