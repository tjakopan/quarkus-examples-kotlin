plugins {
  id("gs-quarkus-conventions")
  `java-library`
}

dependencies {
  compileOnly("io.quarkus:quarkus-hibernate-reactive-panache")
  compileOnly("io.quarkus:quarkus-hibernate-reactive-panache-kotlin")
}