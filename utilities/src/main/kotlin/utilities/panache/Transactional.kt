package utilities.panache

import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.coroutines.asUni
import io.smallrye.mutiny.coroutines.awaitSuspending
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

// Meant to be called from components that do not have explicit coroutine scope in source code, but they do have
// it in runtime, like rest-easy resources.
@OptIn(ExperimentalCoroutinesApi::class)
suspend inline fun <T> withTransaction(crossinline work: suspend CoroutineScope.() -> T): T = coroutineScope {
  Panache.withTransaction { async { work() }.asUni() }.awaitSuspending()
}

// Blocking. Meant to be used from methods that can only be blocking (for example startup observers), but we want
// to use suspending api from those methods.
@OptIn(ExperimentalCoroutinesApi::class)
inline fun <T> CoroutineScope.withTransaction(crossinline work: suspend CoroutineScope.() -> T): T {
  return Panache.withTransaction { async { work() }.asUni() }.await().indefinitely()
}