package com.example

import org.eclipse.microprofile.config.inject.ConfigProperty
import java.util.*
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/greeting")
class GreetingResource(
  @ConfigProperty(name = "greeting.message") private val message: String,
  @ConfigProperty(name = "greeting.suffix", defaultValue = "!") private val suffix: String,
  @ConfigProperty(name = "greeting.name") name: Optional<String> // Config does not work with kotlin's nullable types.
) {
  private val name: String? = name.orElse(null)

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  fun hello(): String = "$message ${name ?: "world"}$suffix"
}