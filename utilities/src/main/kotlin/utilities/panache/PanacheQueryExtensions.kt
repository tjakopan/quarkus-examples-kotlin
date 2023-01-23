package utilities.panache

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheQuery
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.persistence.NoResultException
import javax.persistence.NonUniqueResultException

@Throws(NonUniqueResultException::class)
suspend fun <Entity : Any> PanacheQuery<Entity>.singleResultOrNullSuspending(): Entity? =
  try {
    this.singleResult().awaitSuspending()
  } catch (e: NoResultException) {
    null
  } catch (e: NonUniqueResultException) {
    throw e
  }

fun <Entity : Any> PanacheQuery<Entity>.singleResultOrNull(): Uni<Entity?> =
  this.singleResult().onFailure(NoResultException::class.java).recoverWithNull()