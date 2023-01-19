package com.example

import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.coroutines.asUni
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async

@OptIn(ExperimentalCoroutinesApi::class)
inline fun <T> withTransaction(coroutineScope: CoroutineScope, crossinline work: suspend () -> T): T {
  return Panache.withTransaction { coroutineScope.async { work() }.asUni() }.await().indefinitely()
}