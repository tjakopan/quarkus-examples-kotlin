package com.example

import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.runtime.StartupEvent
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
object Startup {
  fun loadUsers(@Observes evt: StartupEvent): Unit =
    Panache.withTransaction {
      User.deleteAll()
        .flatMap { User.add("admin", "admin", "admin") }
        .flatMap { User.add("user", "user", "user") }
        .map { }
    }.await().indefinitely()
}