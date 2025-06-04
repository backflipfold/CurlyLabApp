package org.example.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.example.models.FavoriteInput
import org.example.models.ReviewInput
import org.example.service.ProductService
import java.util.UUID

fun Route.productRoutes(service: ProductService) {

    route("/products") {
        get {
            call.respond(service.getAllProducts())
        }

        get("/{productId}") {
            val productId = call.parameters["productId"]?.let { UUID.fromString(it) }
                ?: throw IllegalArgumentException("Invalid product ID")
            val product = service.getProductById(productId)

            call.respond(product)
        }

        get("/favorites/{userId}") {
            val userId = call.parameters["userId"]?.let { UUID.fromString(it) }
                ?: throw IllegalArgumentException("Invalid UUID format")

            call.respond(service.getUserFavorites(userId))
        }

        get("/{productId}/is-favorite/{userId}") {
            val productId = call.parameters["productId"]?.let { UUID.fromString(it) }
                ?: throw IllegalArgumentException("Invalid product ID")
            val userId = call.parameters["userId"]?.let { UUID.fromString(it) }
                ?: throw IllegalArgumentException("Invalid user ID")

            call.respond(service.isFavorite(userId, productId))
        }

        post("/favorites") {
            val favorite = call.receive<FavoriteInput>()
            service.addToFavorites(favorite)
            call.respond(HttpStatusCode.Created, "Added to favorites")
        }

        delete("/favorites") {
            val favorite = call.receive<FavoriteInput>()
            service.removeFromFavorites(favorite)
            call.respond(HttpStatusCode.OK, "Removed from favorites")
        }

        post("/rate") {
            val review = call.receive<ReviewInput>()
            try {
                service.addReview(review)
                call.respond(HttpStatusCode.Created, "Review added")
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid rating")
            }
        }

        get("{id}/reviews") {
            val id = call.parameters["id"]?.let { UUID.fromString(it) }
                ?: throw IllegalArgumentException("Invalid UUID format")

            call.respond(service.getReviews(id))
        }

        post("/{productId}/reviews/{userId}/update") {
            val productId = call.parameters["productId"]?.let { UUID.fromString(it) }
                ?: throw IllegalArgumentException("Invalid product ID")
            val userId = call.parameters["userId"]?.let { UUID.fromString(it) }
                ?: throw IllegalArgumentException("Invalid user ID")
            val review = call.receive<ReviewInput>()
            try {
                service.updateReview(userId, productId, review)
                call.respond(HttpStatusCode.OK, "Review updated")
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid review data")
            }
        }

        delete("/{productId}/reviews/{userId}") {
            val productId = call.parameters["productId"]?.let { UUID.fromString(it) }
                ?: throw IllegalArgumentException("Invalid product ID")
            val userId = call.parameters["userId"]?.let { UUID.fromString(it) }
                ?: throw IllegalArgumentException("Invalid user ID")
            try {
                service.deleteReview(userId, productId)
                call.respond(HttpStatusCode.OK, "Review deleted")
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid review data")
            }
        }

    }
}
