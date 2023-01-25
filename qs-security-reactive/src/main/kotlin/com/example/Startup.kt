package com.example

import io.quarkus.logging.Log
import io.quarkus.runtime.StartupEvent
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.mutiny.core.Vertx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import utilities.panache.withTransaction
import utilities.vertx.dispatcher
import javax.annotation.PreDestroy
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import kotlin.coroutines.CoroutineContext

@ApplicationScoped
class Startup(private val vertx: Vertx) : CoroutineScope, AutoCloseable {
  override val coroutineContext: CoroutineContext by lazy { vertx.dispatcher() + SupervisorJob() }

  fun loadUsers(@Observes evt: StartupEvent): Unit = withTransaction {
    Log.info("info")
    User.deleteAll().awaitSuspending()
    User.add("admin", "admin", "admin")
    User.add("user", "user", "user")
  }

  @PreDestroy
  override fun close() {
    coroutineContext.cancel()
  }
}