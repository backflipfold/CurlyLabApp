plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "aps.backflip.curlylab"
version = "0.0.1"

application {
    mainClass.set("aps.backflip.curlylab.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // Google Drive Api
    implementation(libs.google.api.client)
    implementation(libs.google.api.services.drive)
    implementation(libs.google.oauth.client)
    implementation(libs.google.auth.library.oauth2.http)
    implementation(libs.google.http.client.jackson2)

    // Utils
    implementation(libs.snakeyaml)
    implementation(libs.com.auth0)
    implementation(libs.uuid)
    implementation(libs.uuid.serialization)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    // Ktor
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.json)

    // Exposed
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)

    // PostgreSQL
    implementation(libs.postgresql)

    // Logging
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.call.logging)

    // Tests
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

    // Mockito Kotlin

    testImplementation("junit:junit:4.13.2")

    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("org.mockito:mockito-core:5.2.0")

    testImplementation(kotlin("test"))

    testImplementation("com.h2database:h2:2.2.224")
}