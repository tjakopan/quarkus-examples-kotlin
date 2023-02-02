plugins {
  id("gs-quarkus-conventions")
}

dependencies {
  implementation(project(":utilities"))
  implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
  implementation("io.quarkus:quarkus-smallrye-reactive-messaging-kafka")
  implementation("io.quarkus:quarkus-hibernate-reactive-panache-kotlin")
  implementation("io.quarkus:quarkus-reactive-pg-client")
  implementation("io.quarkus:quarkus-smallrye-context-propagation")

  testImplementation("io.rest-assured:rest-assured")
  testImplementation("io.rest-assured:kotlin-extensions")
  testImplementation("org.jboss.resteasy:resteasy-client")
  testImplementation("org.awaitility:awaitility")
}
