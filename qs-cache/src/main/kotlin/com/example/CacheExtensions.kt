package com.example

import io.quarkus.cache.Cache
import io.smallrye.mutiny.Uni
import java.util.concurrent.CompletionStage

suspend inline fun <K, V> Cache.get(key: K, valueLoader: suspend (K) -> V): V {

}

//inline fun <K, V> Cache.get(coroutineScope: CoroutineScope, key: K, crossinline valueLoader: suspend (K) -> V): Uni<V> {
//  return get(key) {k -> coroutineScope.async { valueLoader(k) }}
//}

// Will be in quarkus 2.17.
//inline fun <K, V> Cache.get(key: K, crossinline valueLoader: (K) -> Uni<V>): Uni<V> {
//  this.
//  return Uni.createFrom()
//    .completionStage {
//      if (this.)
//    }
//}

private fun <K, V> Cache.compute(key: K, valueLoader: (K) -> Uni<V>): CompletionStage<V> {
  return get(key) { k ->
    valueLoader(k)
  }.subscribeAsCompletionStage()
}
