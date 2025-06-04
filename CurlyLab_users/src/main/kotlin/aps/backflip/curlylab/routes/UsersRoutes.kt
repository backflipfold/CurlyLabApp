package aps.backflip.curlylab.routes

import aps.backflip.curlylab.controllers.UsersController
import aps.backflip.curlylab.models.UserRequest
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import java.util.UUID

fun Route.usersRoutes(controller: UsersController) {
    route("/users") {
        post {
            val request = call.receive<UserRequest>()
            val userId = controller.createUser(request)
            call.respond(HttpStatusCode.Created, mapOf("id" to userId))
        }

        get {
            val users = controller.getAllUsers()
            call.respond(users)
        }

        get("/{id}") {
            val userId = UUID.fromString(call.parameters["id"])
            val user = controller.getUser(userId)
                ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(user)
        }

        put("/{id}") {
            val userId = UUID.fromString(call.parameters["id"])
            val request = call.receive<UserRequest>()
            controller.updateUser(userId, request)
            call.respond(HttpStatusCode.NoContent)
        }

        post("/{id}/uploadImage") {
            try {
                val userId = UUID.fromString(call.parameters["id"])
                val multipart = call.receiveMultipart()
                var fileBytes: ByteArray? = null
                var fileName: String? = null

                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        fileName = part.originalFileName ?: "avatar.jpg"
                        fileBytes = part.streamProvider().readBytes()
                    }
                    part.dispose()
                }

                if (fileBytes != null && fileName != null) {
                    val imageUrl = controller.uploadUserImage(userId, fileBytes!!, fileName!!)
                    call.respond(HttpStatusCode.OK, mapOf("imageUrl" to imageUrl))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Image file not found")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Internal Server Error: ${e.message}")
            }
        }


        delete("/{id}") {
            val userId = UUID.fromString(call.parameters["id"])
            controller.deleteUser(userId)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}