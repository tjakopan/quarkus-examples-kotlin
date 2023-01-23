package utilities.panache

import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.coroutines.asUni
import io.smallrye.mutiny.coroutines.awaitSuspending
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalCoroutinesApi::class)
suspend inline fun <T> withTransaction(crossinline work: suspend () -> T): T = coroutineScope {
  Panache.withTransaction { async { work() }.asUni() }.awaitSuspending()
}

@OptIn(ExperimentalCoroutinesApi::class)
inline fun <T> withTransaction(coroutineScope: CoroutineScope, crossinline work: suspend () -> T): T {
  return Panache.withTransaction { coroutineScope.async { work() }.asUni() }.await().indefinitely()
}