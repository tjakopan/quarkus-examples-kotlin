plugins {
  id("gs-quarkus-conventions")
}

dependencies {
  implementation("io.quarkus:quarkus-resteasy-reactive")

  testImplementation("io.rest-assured:rest-assured")
  testImplementation("io.rest-assured:kotlin-extensions")
}
