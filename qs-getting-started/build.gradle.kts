plugins {
  kotlin("jvm") version "1.7.21"
  kotlin("plugin.allopen") version "1.7.21"
  id("io.quarkus")
}

repositories {
  mavenCentral()
  mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
  implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
  implementation("io.quarkus:quarkus-resteasy-reactive")
  implementation("io.quarkus:quarkus-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("io.quarkus:quarkus-arc")

  testImplementation(kotlin("test"))
  testImplementation("io.quarkus:quarkus-junit5")
  testImplementation("io.rest-assured:rest-assured")
  testImplementation("io.rest-assured:kotlin-extensions")
}

group = "com.example"
version = "1.0-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_18
  targetCompatibility = JavaVersion.VERSION_18
}

tasks.withType<Test> {
  systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
allOpen {
  annotation("javax.ws.rs.Path")
  annotation("javax.enterprise.context.ApplicationScoped")
  annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions.jvmTarget = JavaVersion.VERSION_18.toString()
  kotlinOptions.javaParameters = true
}
