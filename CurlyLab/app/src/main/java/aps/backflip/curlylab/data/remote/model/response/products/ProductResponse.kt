package aps.backflip.curlylab.data.remote.model.response.products

import aps.backflip.curlylab.domain.model.products.Product
import aps.backflip.curlylab.utils.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ProductResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val description: String,
    val tags: List<String>,
    val imageUrl: String?
) {
    fun toDomain() = Product(
        id = id,
        name = name,
        description = description,
        tags = tags,
        imageUrl = imageUrl
    )
}
