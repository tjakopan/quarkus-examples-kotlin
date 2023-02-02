plugins {
  id("gs-quarkus-conventions")
}

dependencies {
  implementation(project(":utilities"))
  implementation(project(":qs-kafka:common"))
  implementation("io.quarkus:quarkus-smallrye-reactive-messaging-kafka")
  implementation("io.vertx:vertx-lang-kotlin-coroutines")
}
