package aps.backflip.curlylab.data.remote.model.response.profile

import aps.backflip.curlylab.utils.serializers.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class UserResponse(
    val id: String,
    val username: String,
    val language: String,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    val imageUrl: String?
)