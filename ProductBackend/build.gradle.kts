plugins {
    kotlin("jvm") version "1.7.10"
    application
    kotlin("plugin.serialization") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Ktor
    implementation("io.ktor:ktor-server-core-jvm:2.1.0")
    implementation("io.ktor:ktor-server-netty-jvm:2.1.0")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.1.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.1.0")

    // Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.41.1")

    // PostgreSQL Driver
    implementation("org.postgresql:postgresql:42.3.1")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.2.11")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
}

application {
    mainClass.set("com.example.ApplicationKt")
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set("product-service")
        archiveClassifier.set("")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to application.mainClass.get()))
        }
    }
}

tasks.assemble {
    dependsOn("shadowJar")
}
