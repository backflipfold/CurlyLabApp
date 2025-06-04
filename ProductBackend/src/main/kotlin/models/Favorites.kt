package org.example.models

import kotlinx.serialization.Serializable
import org.example.serializers.UUIDSerializer
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import java.util.UUID

object Favorites : Table() {
    val userId = uuid("user_id")
    val productId = uuid("product_id").references(Products.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(userId, productId, name = "pk_favorites")
}

@Serializable
data class FavoriteInput(
    @Serializable(with = UUIDSerializer::class) val userId: UUID,
    @Serializable(with = UUIDSerializer::class) val productId: UUID
)