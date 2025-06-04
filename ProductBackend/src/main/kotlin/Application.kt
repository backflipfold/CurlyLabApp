package org.example

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.example.repository.ProductRepository
import org.example.routes.productRoutes
import org.example.service.ProductService

fun main() {
    embeddedServer(Netty, port = 8083) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json { prettyPrint = true })
    }

    DatabaseFactory.init()

    val productRepository = ProductRepository()
    val productService = ProductService(productRepository)

    routing {
        productRoutes(productService)
    }
}
