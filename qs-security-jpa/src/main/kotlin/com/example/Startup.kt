package com.example

import io.quarkus.runtime.StartupEvent
import javax.enterprise.event.Observes
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
object Startup {
  @Transactional
  fun loadUsers(@Observes evt: StartupEvent) {
    User.deleteAll()
    User.add("admin", "admin", "admin")
    User.add("user", "user", "user")
  }
}