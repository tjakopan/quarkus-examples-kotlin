plugins {
  id("gs-quarkus-conventions")
}

dependencies {
  implementation(project(":utilities"))
  implementation("io.quarkus:quarkus-resteasy-reactive")
  implementation("io.quarkus:quarkus-hibernate-reactive-panache-kotlin")
  implementation("io.quarkus:quarkus-reactive-pg-client")
  implementation("io.quarkus:quarkus-security")
  implementation("io.quarkus:quarkus-elytron-security-common")

  testImplementation("io.rest-assured:rest-assured")
  testImplementation("io.rest-assured:kotlin-extensions")
}
