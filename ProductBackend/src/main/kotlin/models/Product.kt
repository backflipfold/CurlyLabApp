package org.example.models

import kotlinx.serialization.Serializable
import org.example.serializers.UUIDSerializer
import org.jetbrains.exposed.sql.Table
import java.util.UUID

object Products : Table() {
    val id = uuid("id").clientDefault { UUID.randomUUID() }
    val name = varchar("name", 255)
    val description = text("description")
    val tags = text("tags")
    val imageUrl = varchar("image_url", 512).nullable()

    override val primaryKey = PrimaryKey(id, name = "pk_products_id")
}

@Serializable
data class ProductDTO(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val description: String,
    val tags: List<String>,
    val imageUrl: String?
)