package org.example.repository

import io.ktor.server.plugins.NotFoundException
import kotlinx.serialization.json.Json
import org.example.models.FavoriteInput
import org.example.models.Favorites
import org.example.models.ProductDTO
import org.example.models.Products
import org.example.models.ReviewDTO
import org.example.models.ReviewInput
import org.example.models.Reviews
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.UUID
import kotlin.Array
import java.sql.Array as SqlArray

class ProductRepository {
    private fun parseTags(raw: Any?): List<String> {
        return when (raw) {
            is String -> raw
                .removePrefix("{")
                .removeSuffix("}")
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            is SqlArray -> {
                (raw.array as? Array<*>)
                    ?.mapNotNull { it?.toString()?.trim() }
                    ?: emptyList()
            }

            else -> emptyList()
        }
    }

    fun getAllProducts(): List<ProductDTO> = transaction {
        Products.selectAll().map {
            ProductDTO(
                id = it[Products.id],
                name = it[Products.name],
                description = it[Products.description],
                tags = parseTags(it[Products.tags]),
                imageUrl = it[Products.imageUrl]
            )
        }
    }

    fun getProductById(productId: UUID): ProductDTO = transaction {
        Products.select { Products.id eq productId }
            .map {
                ProductDTO(
                    id = it[Products.id],
                    name = it[Products.name],
                    description = it[Products.description],
                    tags = parseTags(it[Products.tags]),
                    imageUrl = it[Products.imageUrl]
                )
            }
            .firstOrNull() ?: throw NotFoundException("Product with ID $productId not found")
    }

    fun addReview(review: ReviewInput) = transaction {
        Reviews.insert {
            it[productId] = review.productId
            it[userId] = review.userId
            it[userName] = review.userName
            it[rating] = review.rating
            it[comment] = review.comment
        }
    }

    fun updateReview(userId: UUID, productId: UUID, review: ReviewInput) = transaction {
        Reviews.update(
            where = { (Reviews.userId eq userId) and (Reviews.productId eq productId) }
        ) {
            it[userName] = review.userName
            it[rating] = review.rating
            it[comment] = review.comment
        }
    }

    fun getReviews(productId: UUID): List<ReviewDTO> = transaction {
        Reviews.select { Reviews.productId eq productId }
            .map {
                ReviewDTO(
                    id = it[Reviews.id],
                    productId = it[Reviews.productId],
                    userId = it[Reviews.userId],
                    userName = it[Reviews.userName],
                    rating = it[Reviews.rating],
                    comment = it[Reviews.comment],
                    createdAt = it[Reviews.createdAt].toString()
                )
            }
    }

    fun removeReview(userId: UUID, productId: UUID) = transaction {
        Reviews.deleteWhere {
            (Reviews.userId eq userId) and (Reviews.productId eq productId)
        }
    }

    fun addToFavorites(favorite: FavoriteInput) = transaction {
        Favorites.insert {
            it[userId] = favorite.userId
            it[productId] = favorite.productId
        }
    }

    fun removeFromFavorites(favorite: FavoriteInput) = transaction {
        Favorites.deleteWhere {
            (Favorites.userId eq favorite.userId) and
                    (Favorites.productId eq favorite.productId)
        }
    }

    fun getUserFavorites(userId: UUID): List<ProductDTO> = transaction {
        (Products innerJoin Favorites)
            .select { Favorites.userId eq userId }
            .map {
                ProductDTO(
                    id = it[Products.id],
                    name = it[Products.name],
                    description = it[Products.description],
                    tags = parseTags(it[Products.tags]),
                    imageUrl = it[Products.imageUrl]
                )
            }
    }

    fun isFavorite(userId: UUID, productId: UUID): Boolean = transaction {
        Favorites.select {
            (Favorites.userId eq userId) and (Favorites.productId eq productId)
        }.count() > 0
    }
}