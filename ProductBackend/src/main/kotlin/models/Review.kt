package org.example.models

import kotlinx.serialization.Serializable
import org.example.serializers.UUIDSerializer
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.util.UUID

object Reviews : Table() {
    val id = uuid("id").clientDefault { UUID.randomUUID() }
    val productId = uuid("product_id")
    val userId = uuid("user_id")
    val userName = text("user_name")
    val rating = integer("rating")
    val comment = text("comment")
    val createdAt = timestamp("created_at").clientDefault { java.time.Instant.now() }

    override val primaryKey = PrimaryKey(id, name = "pk_reviews_id")
}

@Serializable
data class ReviewDTO(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val productId: UUID,
    @Serializable(with = UUIDSerializer::class) val userId: UUID,
    val userName: String,
    val rating: Int,
    val comment: String,
    val createdAt: String? = null
)

@Serializable
data class ReviewInput(
    @Serializable(with = UUIDSerializer::class) val productId: UUID,
    @Serializable(with = UUIDSerializer::class) val userId: UUID,
    val userName: String,
    val rating: Int,
    val comment: String
)