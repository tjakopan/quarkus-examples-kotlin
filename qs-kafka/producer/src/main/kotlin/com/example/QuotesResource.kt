package com.example

import io.quarkus.smallrye.reactivemessaging.sendSuspending
import io.smallrye.mutiny.Multi
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.jboss.resteasy.reactive.RestStreamElementType
import java.util.*
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/quotes")
class QuotesResource(
    @Channel("quote-requests") private val quoteRequestEmitter: Emitter<String>,
    @Channel("quotes") private val quotes: Multi<Quote>
) {
    // Endpoint to generate a new quote request id and send it to "quote-requests" Kafka topic using emitter.
    @POST
    @Path("/request")
    @Produces(MediaType.TEXT_PLAIN)
    suspend fun createRequest(): String {
        val uuid = UUID.randomUUID()
        quoteRequestEmitter.sendSuspending(uuid.toString())
        return uuid.toString()
    }

    // Endpoint retrieving the "quotes" Kafka topic and sending the items to a server sent event.
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    fun stream(): Multi<Quote> = quotes.log()
}