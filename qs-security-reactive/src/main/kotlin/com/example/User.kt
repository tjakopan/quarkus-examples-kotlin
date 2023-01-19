package com.example

import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "test_user")
class User : PanacheEntity() {
  lateinit var username: String
  lateinit var password: String
  lateinit var role: String

  companion object : PanacheCompanion<User> {
    suspend fun add(username: String, password: String, role: String): User {
      val user = User().apply {
        this.username = username
        this.password = BcryptUtil.bcryptHash(password)
        this.role = role
      }
      return user.persist<User>().awaitSuspending()
    }

    suspend fun findByUsername(username: String): User? = find("username", username).singleResultOrNull()
  }
}