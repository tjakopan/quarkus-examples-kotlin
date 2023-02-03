package com.example

import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands
import io.quarkus.redis.datasource.value.ReactiveValueCommands
import io.smallrye.mutiny.coroutines.awaitSuspending
import utilities.mutiny.replaceWithUnit
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class IncrementService(@Suppress("CdiInjectionPointsInspection") dataSource: ReactiveRedisDataSource) {
  private val keyCommands: ReactiveKeyCommands<String> = dataSource.key()
  private val countCommands: ReactiveValueCommands<String, Long> = dataSource.value(Long::class.javaObjectType)
//  private val countCommands: ReactiveValueCommands<String, Long> = dataSource.value(Long::class.java)

  suspend fun get(key: String): Long = countCommands[key].awaitSuspending() ?: 0L

  suspend fun set(key: String, value: Long) = countCommands.set(key, value).replaceWithUnit().awaitSuspending()

  suspend fun increment(key: String, incrementBy: Long) =
    countCommands.incrby(key, incrementBy).replaceWithUnit().awaitSuspending()

  suspend fun del(key: String) = keyCommands.del(key).replaceWithUnit().awaitSuspending()

  suspend fun keys(): List<String> = keyCommands.keys("*").awaitSuspending()
}
