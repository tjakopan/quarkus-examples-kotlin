plugins {
  id("gs-quarkus-conventions")
}

dependencies {
  implementation(project(":utilities"))
  implementation("io.quarkus:quarkus-redis-client")
  implementation("io.quarkus:quarkus-resteasy-reactive-jackson")

  testImplementation("io.rest-assured:rest-assured")
  testImplementation("io.rest-assured:kotlin-extensions")
}
