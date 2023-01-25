plugins {
  id("gs-quarkus-conventions")
}

dependencies {
  implementation(project(":utilities"))
  implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
  implementation("io.quarkus:quarkus-resteasy-reactive-kotlin")
  implementation("io.quarkus:quarkus-cache")
  implementation("io.vertx:vertx-lang-kotlin-coroutines")

  testImplementation("io.rest-assured:rest-assured")
  testImplementation("io.rest-assured:kotlin-extensions")
}
