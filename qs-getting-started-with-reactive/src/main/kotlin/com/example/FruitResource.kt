package com.example

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.CompositeException
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.jboss.logging.Logger
import utilities.panache.withTransaction
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class FruitResource {
  @GET
  suspend fun get(): List<Fruit> = Fruit.listAll(Sort.by("name")).awaitSuspending()

  @GET
  @Path("/{id}")
  suspend fun getSingle(id: Long): Fruit? = Fruit.findById(id).awaitSuspending()

  @POST
  suspend fun create(fruit: Fruit): Response = withTransaction {
    if (fruit.id != null) throw WebApplicationException("Id was invalidly set on request.", 422)

    val entity = fruit.persist<Fruit>().awaitSuspending()
    Response.ok().status(Status.CREATED).entity(entity).build()
  }

  @PUT
  @Path("/{id}")
  suspend fun update(id: Long, fruit: Fruit): Response = withTransaction {
    val entity = Fruit.findById(id).awaitSuspending()
    if (entity != null) {
      entity.name = fruit.name
      Response.ok(entity).build()
    } else {
      Response.status(Status.NOT_FOUND).build()
    }
  }


  @DELETE
  @Path("/{id}")
  suspend fun delete(id: Long): Response = withTransaction {
    val deleted = Fruit.deleteById(id).awaitSuspending()
    if (deleted) Response.status(Status.NO_CONTENT).build()
    else Response.status(Status.NOT_FOUND).build()
  }

  @Provider
  class ErrorMapper(private val objectMapper: ObjectMapper) : ExceptionMapper<Exception> {
    private val logger = Logger.getLogger(ErrorMapper::class.java)

    override fun toResponse(exception: Exception): Response {
      logger.error("Failed to handle request", exception)

      var throwable = exception as Throwable
      var code = 500
      if (throwable is WebApplicationException) code = throwable.response.status
      if (throwable is CompositeException) throwable = throwable.cause!!

      val exceptionJson = objectMapper.createObjectNode().apply {
        this.put("exceptionType", throwable.javaClass.name)
        this.put("code", code)
        if (exception.message != null) this.put("error", exception.message)
      }

      return Response.status(code).entity(exceptionJson).build()
    }
  }
}