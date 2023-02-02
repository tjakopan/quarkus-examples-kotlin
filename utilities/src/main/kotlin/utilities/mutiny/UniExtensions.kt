package utilities.mutiny

import io.smallrye.mutiny.Uni

fun Uni<Void>.replaceWithUnit(): Uni<Unit> = onItem().transform { }