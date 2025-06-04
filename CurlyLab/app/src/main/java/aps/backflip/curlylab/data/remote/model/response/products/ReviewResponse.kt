package aps.backflip.curlylab.data.remote.model.response.products

import aps.backflip.curlylab.domain.model.products.Review
import aps.backflip.curlylab.utils.serializers.OffsetDateTimeSerializer
import aps.backflip.curlylab.utils.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.util.UUID

@Serializable
data class ReviewResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    val productId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val userName: String,
    val rating: Int,
    val comment: String,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val createdAt: OffsetDateTime
) {
    fun toDomain() = Review(
        id = id,
        productId = productId,
        userId = userId,
        userName = userName,
        rating = rating,
        comment = comment,
        createdAt = createdAt
    )
}