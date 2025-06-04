package aps.backflip.curlylab

import aps.backflip.curlylab.config.ApplicationConfig
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8081) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    with(ApplicationConfig) {
        configure()
    }
}