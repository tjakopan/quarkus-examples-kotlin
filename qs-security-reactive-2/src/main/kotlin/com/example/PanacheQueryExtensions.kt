package com.example

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheQuery
import io.smallrye.mutiny.Uni
import javax.persistence.NoResultException

fun <Entity : Any> PanacheQuery<Entity>.singleResultOrNull(): Uni<Entity?> =
  this.singleResult().onFailure(NoResultException::class.java).recoverWithNull()