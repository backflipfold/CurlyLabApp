package aps.backflip.curlylab.routes

import aps.backflip.curlylab.controllers.AuthController
import aps.backflip.curlylab.models.LoginRequest
import aps.backflip.curlylab.models.RefreshTokenRequest
import aps.backflip.curlylab.models.RegisterRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoutes(authController: AuthController) {
    route("/auth") {
        post("/register") {
            try {
                val request = call.receive<RegisterRequest>()
                val response = authController.register(request)
                call.respond(HttpStatusCode.Created, response)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        post("/login") {
            try {
                val request = call.receive<LoginRequest>()
                val response = authController.login(request)
                call.respond(response)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid credentials"))
            }
        }

        // Обновление токена
        post("/refresh") {
            try {
                val request = call.receive<RefreshTokenRequest>()
                val response = authController.refresh(request)
                call.respond(response)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid refresh token"))
            }
        }

        // Выход с инвалидацией токена
        post("/logout") {
            val authHeader = call.request.headers["Authorization"]
            authHeader?.let { token ->
                authController.invalidateToken(token.removePrefix("Bearer "))
                call.respond(HttpStatusCode.OK)
            } ?: call.respond(HttpStatusCode.BadRequest)
        }
    }
}