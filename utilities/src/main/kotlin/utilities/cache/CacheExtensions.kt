package utilities.cache

import io.quarkus.cache.Cache
import io.quarkus.cache.runtime.NullValueConverter
import io.quarkus.cache.runtime.caffeine.CaffeineCacheImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.await
import java.util.concurrent.CompletableFuture

// Left as an example but found another way via callService and runService CoroutineScope extensions.
@Suppress("UNCHECKED_CAST")
suspend fun <K, V> Cache.getSuspending(key: K, valueLoader: suspend CoroutineScope.(K) -> V): V = coroutineScope {
  val cache = `as`(CaffeineCacheImpl::class.java)
  val future = cache.getIfPresent<V>(key)
  if (future != null) future.await()
  else {
    val newValue = NullValueConverter.toCacheValue(valueLoader(key)) as V
    cache.put(key, CompletableFuture.completedFuture(newValue))
    NullValueConverter.fromCacheValue(newValue) as V
  }
}
