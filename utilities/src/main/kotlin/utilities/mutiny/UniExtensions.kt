package utilities.mutiny

import io.smallrye.mutiny.Uni

fun <T> Uni<T>.replaceWithUnit(): Uni<Unit> = onItem().transform { }
