package com.example

import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import io.smallrye.mutiny.Uni
import javax.persistence.Entity
import javax.persistence.NoResultException
import javax.persistence.Table

@Suppress("JpaDataSourceORMInspection")
@Entity
@Table(name = "test_user")
class User : PanacheEntity() {
  lateinit var username: String
  lateinit var password: String
  lateinit var role: String

  companion object : PanacheCompanion<User> {
    fun add(username: String, password: String, role: String): Uni<User> {
      val user = User().apply {
        this.username = username
        this.password = BcryptUtil.bcryptHash(password)
        this.role = role
      }
      return user.persist()
    }

    fun findByUsername(username: String): Uni<User?> =
      find("username", username).singleResult().onFailure(NoResultException::class.java).recoverWithNull()
  }
}