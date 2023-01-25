package com.example

import io.quarkus.runtime.StartupEvent
import io.smallrye.mutiny.coroutines.awaitSuspending
import utilities.panache.withTransaction
import utilities.service.AbstractService
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class Startup : AbstractService() {
  fun loadUsers(@Observes evt: StartupEvent): Unit = withTransaction {
    User.deleteAll().awaitSuspending()
    User.add("admin", "admin", "admin")
    User.add("user", "user", "user")
  }
}