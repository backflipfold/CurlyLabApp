package aps.backflip.curlylab.data.remote.model.request.products

import aps.backflip.curlylab.utils.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ReviewRequest(
    @Serializable(with = UUIDSerializer::class)
    val productId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val userName: String,
    val rating: Int,
    val comment: String
)