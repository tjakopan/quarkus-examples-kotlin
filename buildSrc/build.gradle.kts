plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.21")
  implementation("org.jetbrains.kotlin:kotlin-allopen:1.7.21")
  implementation("io.quarkus:gradle-application-plugin:2.15.3.Final")
}