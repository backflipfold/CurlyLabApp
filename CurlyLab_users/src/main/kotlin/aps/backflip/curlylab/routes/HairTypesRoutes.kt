package aps.backflip.curlylab.routes

import aps.backflip.curlylab.controllers.HairTypesController
import aps.backflip.curlylab.models.HairTypeRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import java.util.UUID

fun Route.hairTypesRoutes(controller: HairTypesController) {
    route("/hairtypes") {
        put("/{userId}") {
            val userId = UUID.fromString(call.parameters["userId"])
            val request = call.receive<HairTypeRequest>()
            controller.updateHairType(userId, request)
            call.respond(HttpStatusCode.NoContent)
        }

        get("/{id}") {
            val userId = UUID.fromString(call.parameters["id"])
            val profile = controller.getHairType(userId)
                ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(profile)
        }

        delete("/{userId}") {
            val userId = UUID.fromString(call.parameters["userId"])
            controller.deleteHairType(userId)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}