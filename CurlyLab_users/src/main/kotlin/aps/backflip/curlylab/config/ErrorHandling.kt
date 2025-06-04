package aps.backflip.curlylab.config

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import java.util.UUID

class UserNotFoundException(userId: UUID) :
    RuntimeException("User $userId not found")

class ValidationException(errors: Map<String, String>) :
    RuntimeException(errors.entries.joinToString { "${it.key}: ${it.value}" })

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is UserNotFoundException -> {
                    call.respond(
                        HttpStatusCode.NotFound,
                        mapOf("error" to cause.message)
                    )
                }
                is ValidationException -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("errors" to cause.message?.split(", "))
                    )
                }
                else -> {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        mapOf("error" to "Internal server error")
                    )
                }
            }
        }
    }
}