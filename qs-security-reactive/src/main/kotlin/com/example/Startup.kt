package com.example

import io.quarkus.runtime.StartupEvent
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.*
import utilities.panache.withTransaction
import javax.annotation.PreDestroy
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class Startup(@Suppress("CdiInjectionPointsInspection") private val vertx: Vertx) : AutoCloseable {
  private val coroutineScope = CoroutineScope(vertx.dispatcher() + SupervisorJob())

  fun loadUsers(@Observes evt: StartupEvent): Unit = withTransaction(coroutineScope) { loadUsers() }

  private suspend fun loadUsers() {
    User.deleteAll().awaitSuspending()
    User.add("admin", "admin", "admin")
    User.add("user", "user", "user")
  }

  @PreDestroy
  override fun close() {
    coroutineScope.cancel()
  }
}