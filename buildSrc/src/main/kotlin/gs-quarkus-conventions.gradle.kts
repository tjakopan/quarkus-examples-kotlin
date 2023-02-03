import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  kotlin("plugin.allopen")
  id("io.quarkus")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:2.16.1.Final"))
  implementation("io.quarkus:quarkus-kotlin")
  implementation("io.quarkus:quarkus-arc")

  testImplementation(kotlin("test"))
  testImplementation("io.quarkus:quarkus-junit5")
  testImplementation("io.kotest:kotest-assertions-core:5.5.4")
}

java {
  sourceCompatibility = JavaVersion.VERSION_18
  targetCompatibility = JavaVersion.VERSION_18
}

tasks.withType<Test> {
  systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
  useJUnitPlatform() {
    systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
  }
}

allOpen {
  annotation("javax.ws.rs.Path")
  annotation("javax.enterprise.context.ApplicationScoped")
  annotation("javax.persistence.Entity")
  annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = JavaVersion.VERSION_18.toString()
    javaParameters = true
  }
}
